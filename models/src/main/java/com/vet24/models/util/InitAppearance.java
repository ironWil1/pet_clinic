package com.vet24.models.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;

@Component
public class InitAppearance {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    private EntityTransaction txn = null;

    private final String SQL_COLOR = "CREATE TABLE if not exists pet_color (id serial  PRIMARY KEY," +
            "            color TEXT not null unique )";
    private final String SQL_BREED = "CREATE TABLE if not exists  pet_breed (id serial  PRIMARY KEY," +
            "pet_type TEXT not null, breed TEXT not null, UNIQUE (pet_type,breed));";

    @PostConstruct
    public void colorTable() {
        try {
            EntityManager manager = entityManagerFactory.createEntityManager();
            txn = manager.getTransaction();
            txn.begin();
            manager.createNativeQuery(SQL_COLOR).executeUpdate();
            manager.createNativeQuery(SQL_BREED).executeUpdate();
            txn.commit();
        } catch (Throwable e) {
            if (txn != null && txn.isActive()) {
                txn.rollback();
            }
            throw e;
        }
    }

}
