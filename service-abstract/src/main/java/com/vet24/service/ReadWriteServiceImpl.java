package com.vet24.service;

import com.vet24.dao.ReadOnlyDao;
import com.vet24.dao.ReadWriteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class ReadWriteServiceImpl<K extends Serializable, T> extends ReadOnlyServiceImpl<K, T> implements ReadWriteService<K, T> {

    private final ReadWriteDao<K, T> readWriteDao;

    @Autowired
    public ReadWriteServiceImpl(ReadOnlyDao<K, T> readOnlyDao, ReadWriteDao<K, T> readWriteDao) {
        super(readOnlyDao);
        this.readWriteDao = readWriteDao;
    }

    @Override
    public void persist(T entity) {
        readWriteDao.persist(entity);
    }

    @Override
    public void persistAll(List<T> entities) {
        readWriteDao.persistAll(entities);
    }

    @Override
    public T update(T entity) {
        return readWriteDao.update(entity);
    }

    @Override
    public List<T> updateAll(List<T> entities) {
        return readWriteDao.updateAll(entities);
    }

    @Override
    public void delete(T entity) {
        readWriteDao.delete(entity);
    }

    @Override
    public void deleteAll(List<T> entities) {
        readWriteDao.deleteAll(entities);
    }
}
