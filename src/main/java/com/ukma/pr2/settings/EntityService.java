package com.ukma.pr2.settings;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class EntityService {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Object entity) {
        entityManager.persist(entity);
    }

    public <T> T findById(Class<T> entityClass, Object primaryKey) {
        return entityManager.find(entityClass, primaryKey);
    }

    public Object update(Object entity) {
        return entityManager.merge(entity);
    }

    public void delete(Object entity) {
        entityManager.remove(entity);
    }

    public void detach(Object entity) {
        entityManager.detach(entity);
    }

    public void updateFromDB(Object entity) {
        entityManager.refresh(entity);
    }

}
