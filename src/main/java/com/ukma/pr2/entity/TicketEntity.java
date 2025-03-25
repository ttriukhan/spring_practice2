package com.ukma.pr2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class TicketEntity {

    @Id
    @Column(name="id")
    private Integer id;

    @Column(name="owner_name", nullable = false)
    private String ownerName;

    @Column(name="owner_instagram", nullable = false)
    private String ownerInstagram;

    @Column(name="owner_age", nullable = false)
    private Integer ownerAge;

    @Column(name="is_vip")
    private Boolean isVIP;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private EventEntity event;

}
