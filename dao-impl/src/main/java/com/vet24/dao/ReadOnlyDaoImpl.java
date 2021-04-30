package com.vet24.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class ReadOnlyDaoImpl<K extends Serializable, T> implements ReadOnlyDao<K, T> {

    @PersistenceContext
    EntityManager manager;

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public ReadOnlyDaoImpl() {
        this.type = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[1];
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
