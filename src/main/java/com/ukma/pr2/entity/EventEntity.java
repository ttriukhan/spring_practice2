package com.ukma.pr2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="events")
public class EventEntity {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    @EqualsAndHashCode.Exclude
    private String name;

    @Column(name = "event_place")
    @EqualsAndHashCode.Exclude
    private String address;

    @Column(name = "event_date")
    @EqualsAndHashCode.Exclude
    private LocalDate date;

    @Column(name = "start_time")
    @EqualsAndHashCode.Exclude
    private LocalTime start_time;

    @Column(name = "end_time")
    @EqualsAndHashCode.Exclude
    private LocalTime end_time;

    @Column(name = "price", columnDefinition = "NUMERIC(10, 2)")
    @EqualsAndHashCode.Exclude
    private Integer price;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<TicketEntity> tickets = new ArrayList<>();
}
