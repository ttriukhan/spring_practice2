package com.ukma.pr2.tests;

import com.ukma.pr2.entity.EventEntity;
import com.ukma.pr2.entity.TicketEntity;
import com.ukma.pr2.settings.BaseTestWithSetUp;
import com.ukma.pr2.settings.TransactionService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTests extends BaseTestWithSetUp {

    @Autowired
    protected TransactionService transactionService;

    private List<EventEntity> events;
    private int EV_NUM = 3;
    private int TICK_NUM = 5;

    private Integer eventId;
    private List<Integer> ticketsIds;

    @BeforeEach
    void init() {
        events = createEvents(EV_NUM, TICK_NUM);
        EventEntity event = events.get(0);
        eventId = event.getId();
        ticketsIds = event.getTickets().stream().map(TicketEntity::getId).toList();
    }

    @Test
    void testDeleteEventWithTicketsTransactionalSuccess() {
        assertTrue(eventRepository.existsById(eventId));
        List<TicketEntity> tickets = ticketRepository.findAllById(ticketsIds);
        assertNotEquals(0, tickets.size());

        transactionService.deleteEventWithTicketsTransactional(eventId);
        assertEquals( 0, ticketRepository.findAllById(ticketsIds).size());
        assertFalse(eventRepository.existsById(eventId));
    }

    @Test
    void testDeleteEventWithTicketsTransactionalRollback() {
        System.out.println("Initial number of events: " + eventRepository.findAll().size());
        System.out.println("Expected number of events: " + EV_NUM);
        int badId = -1000;
        assertFalse(eventRepository.existsById(badId));
        assertThrows(RuntimeException.class, () ->
                transactionService.deleteEventWithTicketsTransactional(badId));
        assertEquals(TICK_NUM*EV_NUM, ticketRepository.findAll().size());
        assertEquals(EV_NUM, eventRepository.findAll().size());
    }

    @Test
    void testDeleteEventWithTicketsTransactionalTemplateSuccess() {
        assertTrue(eventRepository.existsById(eventId));
        List<TicketEntity> tickets = ticketRepository.findAllById(ticketsIds);
        assertNotEquals(0, tickets.size());

        transactionService.deleteEventWithTicketsTransactionalTemplate(eventId);
        assertEquals(0, ticketRepository.findAllById(ticketsIds).size());
        assertFalse(eventRepository.existsById(eventId));
    }

    @Test
    void testDeleteEventWithTicketsTransactionalTemplateRollback() {
        int badId = -1000;
        assertFalse(eventRepository.existsById(badId));
        assertThrows(RuntimeException.class, () ->
                transactionService.deleteEventWithTicketsTransactional(badId));
        assertEquals(TICK_NUM*EV_NUM, ticketRepository.findAll().size());
        assertEquals(EV_NUM, eventRepository.findAll().size());
    }

    @Test
    void testDeleteEventWithTicketsEntityManagerSuccess() {
        assertTrue(eventRepository.existsById(eventId));
        List<TicketEntity> tickets = ticketRepository.findAllById(ticketsIds);
        assertNotEquals(0, tickets.size());

        transactionService.deleteEventWithTicketsEntityManager(eventId);
        assertEquals(0, ticketRepository.findAllById(ticketsIds).size());
        assertFalse(eventRepository.existsById(eventId));
    }

    @Test
    void testDeleteEventWithTicketsEntityManagerRollback() {
        int badId = -1000;
        assertFalse(eventRepository.existsById(badId));
        assertThrows(RuntimeException.class, () ->
                transactionService.deleteEventWithTicketsTransactional(badId));
        assertEquals(TICK_NUM*EV_NUM, ticketRepository.findAll().size());
        assertEquals(EV_NUM, eventRepository.findAll().size());
    }

}
