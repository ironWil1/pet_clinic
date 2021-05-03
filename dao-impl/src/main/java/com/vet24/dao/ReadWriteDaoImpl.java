package com.vet24.dao;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ReadWriteDaoImpl<K extends Serializable, T> extends ReadOnlyDaoImpl<K, T> {

    public void persist(T entity) {
        manager.persist(entity);
    }

    public void persistAll(List<T> entities) {
        entities.forEach(elem -> manager.persist(elem));
        int count = 0;
        while (entities.listIterator().hasNext()) {
            ++count;
            if (count % 100 == 0) {
                manager.flush();
                manager.clear();
            }
            entities.listIterator().next();
        }
    }

    public T update(T entity) {
        return manager.merge(entity);
    }

    public List<T> updateAll (List<T> entities) {
        return entities.stream().map(elem -> manager.merge(elem)).collect(Collectors.toList());
    }

    public void delete(T entity) {
        manager.remove(entity);
    }

    public void deleteAll(List<T> entities) {
        entities.forEach(elem -> manager.remove(elem));
    }
}
