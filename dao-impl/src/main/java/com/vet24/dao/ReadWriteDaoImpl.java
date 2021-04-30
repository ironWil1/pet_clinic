package com.vet24.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ReadWriteDaoImpl<K extends Serializable, T> extends ReadOnlyDaoImpl<K, T> implements ReadWriteDao<K, T> {

    @PersistenceContext
    EntityManager manager;

    @Override
    public void persist(T entity) {
        manager.persist(entity);
    }

    @Override
    public void persistAll(List<T> entities) {
        entities.forEach(elem -> manager.persist(elem));
    }

    @Override
    public T update(T entity) {
        return manager.merge(entity);
    }

    @Override
    public List<T> updateAll (List<T> entities) {
        return entities.stream().map(elem -> manager.merge(elem)).collect(Collectors.toList());
    }

    @Override
    public void delete(T entity) {
        manager.remove(entity);
    }

    @Override
    public void deleteAll(List<T> entities) {
        entities.forEach(elem -> manager.remove(elem));
    }
}
