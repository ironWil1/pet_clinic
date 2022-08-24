package com.vet24.models.util;

import jdk.jshell.execution.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Component
public class InitAppearance {

    @PersistenceContext
    protected EntityManager manager;

    @Autowired
    EntityManagerFactory entityManagerFactory;
    EntityTransaction txn = null;
    String sql = "CREATE TABLE IF NOT EXISTS color (id BIGINT PRIMARY KEY AUTO_INCREMENT," +
            "color VARCHAR(255))";

    @PostConstruct
    public void colorTable() {
        try {
            manager = entityManagerFactory.createEntityManager();
            txn = manager.getTransaction();
            txn.begin();
            manager.createNativeQuery(sql).executeUpdate();
            txn.commit();
        } catch ( Throwable e ) {
            if ( txn != null && txn.isActive() ) {
                txn.rollback();
            }
            throw e;
        }

    }

    @PostConstruct
    public void BreedTable() {

    }
}
