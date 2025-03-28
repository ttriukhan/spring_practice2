package com.ukma.pr2.settings;

import com.ukma.pr2.entity.EventEntity;
import com.ukma.pr2.entity.EventRepository;
import com.ukma.pr2.entity.TicketEntity;
import com.ukma.pr2.entity.TicketRepository;
import com.ukma.pr2.utils.Utils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class BaseTestWithSetUp extends BaseTest {

    @Autowired
    protected EventRepository eventRepository;

    @Autowired
    protected TicketRepository ticketRepository;

    @Autowired
    protected TransactionService transactionService;

    @Autowired
    protected EntityService entityService;

    @BeforeEach
    public void cleanBefore() {
        ticketRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @AfterEach
    public void cleanAfter() {
        ticketRepository.deleteAll();
        eventRepository.deleteAll();
    }

    protected List<EventEntity> createEvents(int count) {
        List<EventEntity> res = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            EventEntity event = Utils.getRandomEvent();
            eventRepository.saveAndFlush(event);
            res.add(event);
        }
        return res;
    }

    protected List<EventEntity> createEvents(int eventsCount, int ticketsCount) {
        List<EventEntity> events = new ArrayList<>();
        for (int i = 0; i < eventsCount; i++) {
            EventEntity event = Utils.getRandomEvent();
            eventRepository.saveAndFlush(event);
            List<TicketEntity> tickets = new ArrayList<>();
            for (int j = 0; j < ticketsCount; j++) {
                TicketEntity t = Utils.getRandomTicket(event);
                ticketRepository.saveAndFlush(t);
                tickets.add(t);
            }
            event.setTickets(tickets);
            events.add(event);
        }
        return events;
    }

}
