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

    /*
    public <T> void save(T entity) {
        try {
            entityManager.persist(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error saving entity: " + entity.getClass().getSimpleName(), e);
        }
    }

    @Transactional(readOnly = true)
    public <T> Optional<T> findById(Class<T> entityClass, Object primaryKey) {
        try {
            return Optional.ofNullable(entityManager.find(entityClass, primaryKey));
        } catch (Exception e) {
            throw new RuntimeException("Error finding entity with ID: " + primaryKey, e);
        }
    }

    public <T> T update(T entity) {
        try {
            return entityManager.merge(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error updating entity: " + entity.getClass().getSimpleName(), e);
        }
    }

    public <T> void delete(T entity) {
        try {
            if (!entityManager.contains(entity)) {
                entity = entityManager.merge(entity);
            }
            entityManager.remove(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting entity: " + entity.getClass().getSimpleName(), e);
        }
    }

    public <T> void detach(T entity) {
        entityManager.detach(entity);
    }

    public <T> void updateFromDB(T entity) {
        entityManager.refresh(entity);
    }
     */
}
