package com.vet24.service;

import com.vet24.dao.ReadOnlyDao;
import com.vet24.dao.ReadWriteDao;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public abstract class ReadWriteServiceImpl<K extends Serializable, T> extends ReadOnlyServiceImpl<K, T> implements ReadWriteService<K, T> {

    private final ReadWriteDao<K, T> readWriteDao;

    public ReadWriteServiceImpl(ReadOnlyDao<K, T> readOnlyDao, ReadWriteDao<K, T> readWriteDao) {
        super(readOnlyDao);
        this.readWriteDao = readWriteDao;
    }

    @Transactional
    @Override
    public void persist(T entity) {
        readWriteDao.persist(entity);
    }

    @Transactional
    @Override
    public void persistAll(List<T> entities) {
        readWriteDao.persistAll(entities);
    }

    @Transactional
    @Override
    public T update(T entity) {
        return readWriteDao.update(entity);
    }

    @Transactional
    @Override
    public List<T> updateAll(List<T> entities) {
        return readWriteDao.updateAll(entities);
    }

    @Transactional
    @Override
    public void delete(T entity) {
        readWriteDao.delete(entity);
    }

    @Transactional
    @Override
    public void deleteAll(List<T> entities) {
        readWriteDao.deleteAll(entities);
    }
}
