package com.ukma.pr2.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CriteriaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<EventEntity> findEventsByDate(LocalDate eventDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EventEntity> query = cb.createQuery(EventEntity.class);
        Root<EventEntity> root = query.from(EventEntity.class);
        query.select(root).where(cb.equal(root.get(EventEntity_.DATE), eventDate));
        return entityManager.createQuery(query).getResultList();
    }

    public List<EventEntity> mostPopular(int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EventEntity> query = cb.createQuery(EventEntity.class);
        Root<EventEntity> root = query.from(EventEntity.class);
        Join<EventEntity, TicketEntity> tickets = root.join(EventEntity_.TICKETS, JoinType.LEFT);
        query.select(root)
                .groupBy(root.get(EventEntity_.ID))
                .orderBy(cb.desc(cb.count(tickets)))
                .orderBy(cb.asc(root.get(EventEntity_.ID)));
        return entityManager.createQuery(query).setMaxResults(limit).getResultList();
    }

    public List<EventEntity> upcomingEvents() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EventEntity> query = cb.createQuery(EventEntity.class);
        Root<EventEntity> root = query.from(EventEntity.class);
        query.select(root)
                .where(cb.greaterThanOrEqualTo(root.get(EventEntity_.DATE).as(LocalDate.class), LocalDate.now()))
                .orderBy(cb.asc(root.get(EventEntity_.DATE).as(LocalDate.class)));
        return entityManager.createQuery(query).getResultList();
    }

    public List<EventEntity> findEventsThisMonth(String city) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EventEntity> query = cb.createQuery(EventEntity.class);
        Root<EventEntity> root = query.from(EventEntity.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.like(cb.lower(root.get(EventEntity_.ADDRESS)), "%%" + city.toLowerCase() + "%%"));
        predicates.add(
                cb.between(root.get(EventEntity_.DATE),
                LocalDate.now().withDayOfMonth(1),
                LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()))
                );
        query.select(root)
                .where(cb.and(predicates.toArray(new Predicate[0])))
                .orderBy(cb.asc(root.get(EventEntity_.DATE).as(LocalDate.class)));
        return entityManager.createQuery(query).getResultList();
    }
}
