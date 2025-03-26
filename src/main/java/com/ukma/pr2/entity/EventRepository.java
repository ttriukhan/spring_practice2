package com.ukma.pr2.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRepository extends JpaRepository<EventEntity, Integer> {

    @Query("SELECT MAX(e.price) FROM EventEntity e")
    Integer findMaxEventPrice();

    @Query("SELECT MIN(e.price) FROM EventEntity e")
    Integer findMinEventPrice();

}
