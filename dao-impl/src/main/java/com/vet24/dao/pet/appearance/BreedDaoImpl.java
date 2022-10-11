package com.vet24.dao.pet.appearance;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BreedDaoImpl implements BreedDao {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<String> getBreedByPetTypeByBreed(String petType, String breed) {
        List<String> listBreed = new ArrayList<>();
        listBreed.addAll(
                manager.createNativeQuery("SELECT breed from pet_breed where " +
                                "pet_type = :pt and breed % :br")
                        .setParameter("pt", petType)
                        .setParameter("br", breed)
                        .getResultList());
        return listBreed;
    }

    @Override
    public List<String> getBreedByBreed(String breed) {
        List<String> listBreed = new ArrayList<>();
        listBreed.addAll(
                manager.createNativeQuery("SELECT breed from pet_breed where breed % :br ")
                        .setParameter("br", breed).getResultList());
        return listBreed;
    }

    @Override
    public List<String> findAll() {
        List<String> allBreedsList = new ArrayList<>();
        allBreedsList.addAll(manager.createNativeQuery("SELECT breed FROM pet_breed").getResultList());
        return allBreedsList;
    }

    @Override
    public Boolean isPetTypeAndBreedCombinationExist(String petType, String breed) {
        return (Boolean) manager.createNativeQuery("SELECT EXISTS(SELECT pet_type, breed FROM pet_breed WHERE " +
                        "pet_type = :petType AND breed = :breed)")
                .setParameter("petType", petType).setParameter("breed", breed).getSingleResult();
    }
}
