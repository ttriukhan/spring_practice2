package com.ukma.pr2.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<TicketEntity, Integer> {

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM TicketEntity t WHERE t.event.id = :eventId")
    void deleteAllEventTickets(@Param("eventId") Integer eventId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE TicketEntity t SET t.isVIP = TRUE WHERE t.id = :ticketId")
    void makeVip(@Param("ticketId") Integer ticketId);

    @Query("SELECT COUNT(*) FROM TicketEntity t WHERE t.event.id = :eventId")
    int countEventTickets(@Param("eventId") Integer eventId);
}
