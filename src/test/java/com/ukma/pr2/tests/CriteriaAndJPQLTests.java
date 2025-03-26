package com.ukma.pr2.tests;

import com.ukma.pr2.entity.CriteriaRepository;
import com.ukma.pr2.entity.EventEntity;
import com.ukma.pr2.entity.EventRepository;
import com.ukma.pr2.entity.TicketRepository;
import com.ukma.pr2.settings.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(CriteriaRepository.class)
@Sql("/db/et.sql")
public class CriteriaAndJPQLTests extends BaseTest {


    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CriteriaRepository criteriaRepository;

    @Test
    void testDeleteAllTicketsFromEvent() {
        int eventId = 4;
        assertNotEquals(eventRepository.findById(eventId).orElseThrow().getTickets().size(), 0);
        ticketRepository.deleteAllEventTickets(eventId);
        assertEquals(eventRepository.findById(eventId).orElseThrow().getTickets().size(), 0);
    }

    @Test
    void testMakeVip() {
        int ticketId = 101;
        assertFalse(ticketRepository.findById(ticketId).orElseThrow().getIsVIP());
        ticketRepository.makeVip(ticketId);
        assertTrue(ticketRepository.findById(ticketId).orElseThrow().getIsVIP());
    }

    @Test
    void testCountEventTickets() {
        int eventId = 5;
        int actualSize = eventRepository.findById(eventId).orElseThrow().getTickets().size();
        assertEquals(ticketRepository.countEventTickets(eventId), actualSize);
    }

    @Test
    void testFindMaxEventPrice() {
        Integer maxPrice = eventRepository.findMaxEventPrice();
        assertNotNull(maxPrice);
        int actualMax = eventRepository.findAll().stream().max(Comparator.comparing(EventEntity::getPrice)).orElseThrow().getPrice();
        assertEquals(maxPrice, actualMax);
    }

    @Test
    void testFindMinEventPrice() {
        Integer minPrice = eventRepository.findMinEventPrice();
        assertNotNull(minPrice);
        int actualMin = eventRepository.findAll().stream().min(Comparator.comparing(EventEntity::getPrice)).orElseThrow().getPrice();
        assertEquals(minPrice, actualMin);
    }

    @Test
    void testFindEventsByDate() {
        LocalDate date = LocalDate.of(2025, 8, 17);
        List<EventEntity> found = criteriaRepository.findEventsByDate(date);
        List<EventEntity> actual = eventRepository.findAll().stream().filter(event -> event.getDate().equals(date)).toList();
        assertEquals(found, actual);
    }

    @Test
    void testFindEventsMostPopular() {
        int eventNum = 1;
        List<EventEntity> found = criteriaRepository.mostPopular(eventNum);
        List<EventEntity> actual = eventRepository.findAll().stream()
                .sorted(Comparator.comparing(event -> event.getTickets().size(), Comparator.reverseOrder()))
                .sorted(Comparator.comparing(EventEntity::getId)).toList();
        List<EventEntity> sliced = actual.subList(0, Math.min(eventNum, actual.size()));
        assertTrue(found.size() <= eventNum);
        assertEquals(found, sliced);
    }

    @Test
    void testUpcomingEvents() {
        List<EventEntity> found = criteriaRepository.upcomingEvents();
        List<EventEntity> actual = eventRepository.findAll().stream()
                .filter(event -> LocalDateTime.of(event.getDate(), event.getStart_time()).isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(event -> LocalDateTime.of(event.getDate(), event.getStart_time())))
                .toList();
        assertEquals(found, actual);
    }

    @Test
    void testFindThisMonthInCity() {
        String city = "Kyiv";
        List<EventEntity> found = criteriaRepository.findEventsThisMonth(city);
        List<EventEntity> actual = eventRepository.findAll().stream()
                .filter(event -> event.getAddress().toLowerCase().contains(city.toLowerCase()))
                .filter(event -> event.getDate().getMonth().equals(LocalDate.now().getMonth()))
                .sorted(Comparator.comparing(event -> LocalDateTime.of(event.getDate(), event.getStart_time())))
                .toList();
        assertEquals(found, actual);
    }
}
