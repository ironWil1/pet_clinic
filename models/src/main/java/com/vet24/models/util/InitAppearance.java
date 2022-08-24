package com.vet24.models.util;

import jdk.jshell.execution.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
<<<<<<< HEAD
import javax.persistence.EntityTransaction;
=======
>>>>>>> 835b6bca (если executeUpdate оставляет,то выпадает ошибка TransactionRequiredException Executing an update/delete query, если убираю,то табл просто не сохраняется)
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Component
public class InitAppearance {

<<<<<<< HEAD
    //    @PersistenceContext
    @Autowired
    private EntityManager manager;
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    private EntityTransaction txn = null;
    //    String sql = "CREATE TABLE color (id BIGINT PRIMARY KEY AUTO_INCREMENT," +
//            "color VARCHAR(255))";
    String sql2 = "CREATE TABLE if not exists pet_color (id serial  PRIMARY KEY," +
            "            color TEXT not null unique )";
    String sql3 = "CREATE TABLE if not exists  pet_breed (id serial  PRIMARY KEY," +
            "pet_type TEXT not null, breed TEXT not null, UNIQUE (pet_type,breed));";

    // без EntityManagerFactory и EntityTransaction я не смог создать таблицы

    @Transactional
    @PostConstruct
    public void colorTable() {
        try {
            manager = entityManagerFactory.createEntityManager();
            txn = manager.getTransaction();
            txn.begin();
            manager.createNativeQuery(sql2).executeUpdate();
            manager.createNativeQuery(sql3).executeUpdate();
            txn.commit();
        } catch (Throwable e) {
            if (txn != null && txn.isActive()) {
                txn.rollback();
            }
            throw e;
        }
    }


=======
    @PersistenceContext
    protected EntityManager manager;



    @PostConstruct
    public void colorTable() {
        String sql = "CREATE TABLE IF NOT EXISTS color (id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                "color VARCHAR(255))";
        manager.createNativeQuery(sql).executeUpdate();
    }

    @PostConstruct
    public void BreedTable() {

    }
>>>>>>> 835b6bca (если executeUpdate оставляет,то выпадает ошибка TransactionRequiredException Executing an update/delete query, если убираю,то табл просто не сохраняется)
}
