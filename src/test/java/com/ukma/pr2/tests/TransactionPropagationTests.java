package com.ukma.pr2.tests;

import com.ukma.pr2.entity.EventEntity;
import com.ukma.pr2.settings.BaseTestWithSetUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionPropagationTests extends BaseTestWithSetUp {

    private Integer ticketId;

    @BeforeEach
    void init() {
        EventEntity event = createEvents(1, 1).get(0);
        eventRepository.findAll().forEach(System.out::println);
        ticketId = event.getTickets().get(0).getId();
    }

    @Test
    @Transactional
    public void testMandatorySuccess() {
        String insta = "@myinstagram";
        transactionService.changeOwnerInstagramTransactionalMandatory(ticketId, insta);
        eventRepository.findAll().forEach(System.out::println);
        assertEquals(insta, ticketRepository.findById(ticketId).orElseThrow().getOwnerInstagram());
    }

    @Test
    public void testMandatoryRollback() {
        String insta = "@myinstagram";
        assertThrows(IllegalTransactionStateException.class,
                () -> transactionService.changeOwnerInstagramTransactionalMandatory(ticketId, insta));
        eventRepository.findAll().forEach(System.out::println);
        assertNotEquals(insta, ticketRepository.findById(ticketId).orElseThrow().getOwnerInstagram());
    }

    @Test
    public void testNeverSuccess() {
        String insta = "@myinstagram";
        transactionService.changeOwnerInstagramTransactionalNever(ticketId, insta);
        eventRepository.findAll().forEach(System.out::println);
        assertEquals(insta, ticketRepository.findById(ticketId).orElseThrow().getOwnerInstagram());
    }

    @Test
    @Transactional
    public void testNeverRollback() {
        String insta = "@myinstagram";
        assertThrows(IllegalTransactionStateException.class,
                () -> transactionService.changeOwnerInstagramTransactionalNever(ticketId, insta));
        eventRepository.findAll().forEach(System.out::println);
        assertNotEquals(insta, ticketRepository.findById(ticketId).orElseThrow().getOwnerInstagram());
    }

    @Test
    @Transactional
    public void testSupportsSuccessWithTransaction() {
        String insta = "@myinstagram";
        transactionService.changeOwnerInstagramTransactionalSupports(ticketId, insta);
        eventRepository.findAll().forEach(System.out::println);
        assertEquals(insta, ticketRepository.findById(ticketId).orElseThrow().getOwnerInstagram());
    }

    @Test
    public void testSupportsSuccessWithoutTransaction() {
        String insta = "@myinstagram";
        transactionService.changeOwnerInstagramTransactionalSupports(ticketId, insta);
        eventRepository.findAll().forEach(System.out::println);
        assertEquals(insta, ticketRepository.findById(ticketId).orElseThrow().getOwnerInstagram());
    }
}
