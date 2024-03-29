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
    public List<String> getAllColors() {
        return manager.createNativeQuery("SELECT color FROM pet_color").getResultList();
    }

    @Override
    public Boolean isColorExists(String color) {
        return (Boolean) manager.createNativeQuery("SELECT EXISTS(SELECT color FROM pet_color WHERE " +
                "color = :color)").setParameter("color", color).getSingleResult();
    }

    @Override
    public void add(List<String> colors) {
        final String sql = "INSERT INTO pet_color (color) VALUES (:color) ON CONFLICT DO NOTHING;";
        for (String color : colors) {
            manager.createNativeQuery(sql)
                    .setParameter("color", color)
                    .executeUpdate();
        }
    }

    @Override
    public void delete(List<String> colors) {
        final String sql = "DELETE FROM pet_color WHERE color IN (:colors);";
        manager.createNativeQuery(sql)
                .setParameter("colors", colors)
                .executeUpdate();
    }
}
