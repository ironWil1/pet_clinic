package com.vet24.dao.pet.appearance;

import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ColorDaoImpl implements ColorDao{

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<String> getColor(String color) {
        List<String> colorList = new ArrayList<>();
        colorList.addAll(
                manager.createNativeQuery("SELECT color FROM pet_color WHERE color % :cl")
                        .setParameter("cl",color)
                        .getResultList());
        return colorList;
    }


//    @PostConstruct
//    public void test(){
//
//        System.out.println(getColor("blackk"));
//    }

}
