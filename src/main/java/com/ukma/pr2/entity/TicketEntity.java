package com.ukma.pr2.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tickets")
public class TicketEntity {

    @Id
    @Column(name="id")
    private Integer id;

    @Column(name="owner_name", nullable = false)
    @EqualsAndHashCode.Exclude
    private String ownerName;

    @Column(name="owner_instagram", nullable = false)
    @EqualsAndHashCode.Exclude
    private String ownerInstagram;

    @Column(name="owner_age", nullable = false)
    @EqualsAndHashCode.Exclude
    private Integer ownerAge;

    @Column(name="is_vip")
    @EqualsAndHashCode.Exclude
    private Boolean isVIP;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private EventEntity event;

}
