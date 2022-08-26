package com.vet24.dao.pet.appearance;

import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BreedDaoImpl implements BreedDao{

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<String> getBreed(String petType, String breed) {
        List<String> listBreed = new ArrayList<>();
        if (petType.isEmpty()){
            listBreed.addAll(
                    manager.createNativeQuery("SELECT breed from pet_breed where breed % :br ")
                            .setParameter("br",breed).getResultList());
        } else {
            listBreed.addAll(
                    manager.createNativeQuery("SELECT breed from pet_breed where " +
                            "pet_type % :pt and breed % :br")
                            .setParameter("pt",petType)
                            .setParameter("br",breed)
                            .getResultList());
        }
        return listBreed;
    }

//    @PostConstruct
//    void test(){
//        System.out.println(getBreed("dog","бульдог"));
//    }
}
