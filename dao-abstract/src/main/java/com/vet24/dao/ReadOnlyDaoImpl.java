package com.vet24.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

public class ReadOnlyDaoImpl<K extends Serializable, T> implements ReadOnlyDao<K, T> {

    @PersistenceContext
    EntityManager manager;

    private Class<T> type;

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    @Override
    public T getByKey(K key) {
        return manager.find(type, key);
    }

    @Override
    public boolean isExistByKey(K key) {
        return manager.find(type, key) == null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return manager
                .createQuery("FROM " + type.getName())
                .getResultList();
    }
}
