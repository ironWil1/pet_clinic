package com.vet24.models.util;

import jdk.jshell.execution.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Component
public class InitAppearance {

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
}
