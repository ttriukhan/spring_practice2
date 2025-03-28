package com.ukma.pr2.tests;

import com.ukma.pr2.entity.EventEntity;
import com.ukma.pr2.entity.EventRepository;
import com.ukma.pr2.entity.TicketEntity;
import com.ukma.pr2.settings.BaseTestWithSetUp;
import com.ukma.pr2.utils.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.support.TransactionTemplate;


import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionIsolationTests extends BaseTestWithSetUp {

    @Autowired
    protected EventRepository eventRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    List<EventEntity> events;
    private Integer eventId;

    @BeforeEach
    void init() {
        events = createEvents(5);
        eventId = events.get(0).getId();
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    @Test
    public void testReadCommitted() throws InterruptedException {

        assertFalse(checkOnDirtyRead(Isolation.READ_COMMITTED.value()));
        assertTrue(checkNonRepeatableRead(Isolation.READ_COMMITTED.value()));
        assertTrue(checkPhantomRead(Isolation.READ_COMMITTED.value()));

    }

    @Test
    public void testRepeatableRead() throws InterruptedException {

        assertFalse(checkOnDirtyRead(Isolation.REPEATABLE_READ.value()));
        assertFalse(checkNonRepeatableRead(Isolation.REPEATABLE_READ.value()));
        assertFalse(checkPhantomRead(Isolation.REPEATABLE_READ.value()));

    }

    @Test
    public void testSerializable() throws InterruptedException {

        assertFalse(checkOnDirtyRead(Isolation.SERIALIZABLE.value()));
        assertFalse(checkNonRepeatableRead(Isolation.SERIALIZABLE.value()));
        assertFalse(checkPhantomRead(Isolation.SERIALIZABLE.value()));

    }

    boolean checkOnDirtyRead(int i) throws InterruptedException {
        transactionTemplate.setIsolationLevel(i);

        AtomicReference<EventEntity> beforeNonCommitted = new AtomicReference<>();
        AtomicReference<EventEntity> afterNotCommitted = new AtomicReference<>();

        beforeNonCommitted.set(eventRepository.findById(eventId).orElse(null));

        CountDownLatch uncommittedUpdate = new CountDownLatch(1);
        CountDownLatch endReading = new CountDownLatch(1);

        Runnable transaction1 = () -> {
            transactionTemplate.execute(status -> {
                EventEntity ev = eventRepository.findById(eventId).orElseThrow();
                ev.setDate(ev.getDate().minusYears(1));
                eventRepository.saveAndFlush(ev);
                uncommittedUpdate.countDown();
                try {
                    endReading.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                status.setRollbackOnly();
                return 0;
            });
        };

        Runnable transaction2 = () -> {
            transactionTemplate.execute(status -> {
                try {
                    uncommittedUpdate.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                entityManager.clear();
                afterNotCommitted.set(eventRepository.findById(eventId).orElse(null));
                endReading.countDown();
                return 0;
            });
        };

        Thread thread1 = new Thread(transaction1);
        Thread thread2 = new Thread(transaction2);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        //return if there was DR
        return !(beforeNonCommitted.get().equals(afterNotCommitted.get()));
    }


    boolean checkNonRepeatableRead(int isolationLevel) throws InterruptedException {
        transactionTemplate.setIsolationLevel(isolationLevel);

        AtomicReference<Integer> priceOnSite = new AtomicReference<>();
        AtomicReference<Integer> priceToPay = new AtomicReference<>();

        CountDownLatch readPrice = new CountDownLatch(1);
        CountDownLatch priceChangeCommitted = new CountDownLatch(1);

        Runnable transaction1 = () -> {
            transactionTemplate.execute(status -> {
                try {
                    readPrice.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                EventEntity event = eventRepository.findById(eventId).orElseThrow();
                event.setPrice(event.getPrice() + 100);
                eventRepository.saveAndFlush(event);
                priceChangeCommitted.countDown();
                return 0;
            });
        };

        Runnable transaction2 = () -> {
            transactionTemplate.execute(status -> {
                EventEntity event = eventRepository.findById(eventId).orElseThrow();
                priceOnSite.set(event.getPrice());
                readPrice.countDown();
                try {
                    priceChangeCommitted.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                entityManager.clear();
                EventEntity eventAfterUpdate = eventRepository.findById(eventId).orElseThrow();
                priceToPay.set(eventAfterUpdate.getPrice());
                return 0;
            });
        };

        Thread thread1 = new Thread(transaction1);
        Thread thread2 = new Thread(transaction2);

        thread2.start();
        thread1.start();

        thread1.join();
        thread2.join();
        //return if there was NRR
        return !(priceToPay.get().equals(priceOnSite.get()));
    }

    boolean checkPhantomRead(int isolationLevel) throws InterruptedException {
        transactionTemplate.setIsolationLevel(isolationLevel);

        AtomicReference<Integer> ticketsBefore = new AtomicReference<>();
        AtomicReference<Integer> ticketsAfter = new AtomicReference<>();

        CountDownLatch readTickets = new CountDownLatch(1);
        CountDownLatch insertCommitted = new CountDownLatch(1);

        Runnable transaction1 = () -> {
            transactionTemplate.execute(status -> {
                try {
                    readTickets.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                EventEntity ev = eventRepository.findById(eventId).orElseThrow();
                TicketEntity newTicket = Utils.getRandomTicket(ev);
                ticketRepository.saveAndFlush(newTicket);
                insertCommitted.countDown();
                return 0;
            });
        };

        Runnable transaction2 = () -> {
            transactionTemplate.execute(status -> {
                ticketsBefore.set((int) ticketRepository.count());
                readTickets.countDown();
                try {
                    insertCommitted.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                entityManager.clear();
                ticketsAfter.set((int) ticketRepository.count());
                return 0;
            });
        };

        Thread thread1 = new Thread(transaction1);
        Thread thread2 = new Thread(transaction2);

        thread2.start();
        thread1.start();

        thread1.join();
        thread2.join();
        //return if there was FR
        return !(ticketsBefore.get().equals(ticketsAfter.get()));
    }

}
