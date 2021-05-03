package com.vet24.service;

import com.vet24.dao.ReadOnlyDao;

import java.io.Serializable;
import java.util.List;

public abstract class ReadOnlyServiceImpl<K extends Serializable, T> implements ReadOnlyService<K, T> {

    private final ReadOnlyDao<K, T> readOnlyDao;

    public ReadOnlyServiceImpl(ReadOnlyDao<K, T> readOnlyDao) {
        this.readOnlyDao = readOnlyDao;
    }

    @Override
    public T getByKey(K key) {
        return readOnlyDao.getByKey(key);
    }

    @Override
    public boolean isExistByKey(K key) {
        return readOnlyDao.isExistByKey(key);
    }

    @Override
    public List<T> getAll() {
        return readOnlyDao.getAll();
    }
}
