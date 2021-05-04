package com.vet24.service;

import com.vet24.dao.ReadOnlyDaoImpl;

import java.io.Serializable;
import java.util.List;

public abstract class ReadOnlyServiceImpl<K extends Serializable, T> {

    private final ReadOnlyDaoImpl<K, T> readOnlyDao;

    public ReadOnlyServiceImpl(ReadOnlyDaoImpl<K, T> readOnlyDao) {
        this.readOnlyDao = readOnlyDao;
    }

    public T getByKey(K key) {
        return readOnlyDao.getByKey(key);
    }

    public boolean isExistByKey(K key) {
        return readOnlyDao.isExistByKey(key);
    }

    public List<T> getAll() {
        return readOnlyDao.getAll();
    }
}
