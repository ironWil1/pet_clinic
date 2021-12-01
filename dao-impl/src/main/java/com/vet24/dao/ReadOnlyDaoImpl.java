package com.vet24.dao;


import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class ReadOnlyDaoImpl<K extends Serializable, T> {

    @PersistenceContext
    protected EntityManager manager;

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    protected ReadOnlyDaoImpl() {
        this.type = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[1];
    }

    public T getByKey(K key) {
        return manager.find(type, key);
    }

    public boolean isExistByKey(K key) {
        boolean result = false;

        Field id = searchIdInClassAndSuperclasses(type);

        if (id != null) {
            String query = "SELECT CASE WHEN (count(*)>0) then true else false end" +
                    " FROM " + type.getName() + " WHERE " + id.getName() + " = :id";
            result = manager
                    .createQuery(query, Boolean.class)
                    .setParameter("id", key)
                    .getSingleResult();
        }
        return result;
    }

    private Field searchIdInClassAndSuperclasses(Class<?> classForId) {
        Field id = null;
        Field[] declaredFields = classForId.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Id.class)) {
                id = field;
                break;
            }
        }
        if (id == null && !(classForId.equals(Object.class))) {
            classForId = classForId.getSuperclass();
            searchIdInClassAndSuperclasses(classForId);
        }
        return id;
    }

    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return manager
                .createQuery("FROM " + type.getName())
                .getResultList();
    }
}