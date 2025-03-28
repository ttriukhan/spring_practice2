package com.ukma.pr2.settings;

import com.ukma.pr2.entity.EventEntity;
import com.ukma.pr2.entity.EventRepository;
import com.ukma.pr2.entity.TicketEntity;
import com.ukma.pr2.entity.TicketRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@AllArgsConstructor
public class TransactionService {

    private EventRepository eventRepository;
    private TicketRepository ticketRepository;
    private TransactionTemplate transactionTemplate;
    private EntityManagerFactory entityManagerFactory;

    @Transactional
    public void deleteEventWithTicketsTransactional(Integer eventId) {
        EventEntity event = eventRepository.findById(eventId).orElseThrow();
        ticketRepository.deleteAll(event.getTickets());
        eventRepository.delete(event);
    }

    public void deleteEventWithTicketsTransactionalTemplate(Integer eventId) {
        transactionTemplate.executeWithoutResult(status -> {
            EventEntity event = eventRepository.findById(eventId).orElseThrow();
            ticketRepository.deleteAll(event.getTickets());
            eventRepository.delete(event);
        });
    }

    public void deleteEventWithTicketsEntityManager(Integer eventId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        EventEntity event = entityManager.find(EventEntity.class, eventId);
        for (TicketEntity ticket: event.getTickets()) {
            entityManager.remove(ticket);
        }
        entityManager.remove(event);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void changeOwnerInstagramTransactionalMandatory(Integer ticketId, String inst) {
        TicketEntity ticket = ticketRepository.findById(ticketId).orElseThrow();
        ticket.setOwnerInstagram(inst);
    }

    @Transactional(propagation = Propagation.NEVER)
    public void changeOwnerInstagramTransactionalNever(Integer ticketId, String inst) {
        TicketEntity ticket = ticketRepository.findById(ticketId).orElseThrow();
        ticket.setOwnerInstagram(inst);
        ticketRepository.saveAndFlush(ticket);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void changeOwnerInstagramTransactionalSupports(Integer ticketId, String inst) {
        TicketEntity ticket = ticketRepository.findById(ticketId).orElseThrow();
        ticket.setOwnerInstagram(inst);
        ticketRepository.saveAndFlush(ticket);
    }

}
