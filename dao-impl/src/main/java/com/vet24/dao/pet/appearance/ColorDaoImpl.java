package com.vet24.dao.pet.appearance;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ColorDaoImpl implements ColorDao {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<String> findColor(String color) {
        List<String> colorList = new ArrayList<>();
        colorList.addAll(
                manager.createNativeQuery("SELECT color FROM pet_color WHERE color % :cl")
                        .setParameter("cl", color)
                        .getResultList());
        return colorList;
    }

    @Override
    public List<String> findAll() {
        List<String> allColorsList = new ArrayList<>();
        allColorsList.addAll(manager.createNativeQuery("SELECT color FROM pet_color").getResultList());
        return allColorsList;
    }

    @Override
    public Boolean isColorExists(String color) {
        return (Boolean) manager.createNativeQuery("SELECT EXISTS(SELECT color FROM pet_color WHERE " +
                "color = :color)").setParameter("color", color).getSingleResult();
    }
}
