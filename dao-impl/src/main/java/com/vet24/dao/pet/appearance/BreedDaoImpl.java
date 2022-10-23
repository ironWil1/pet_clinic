package com.vet24.dao.pet.appearance;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BreedDaoImpl implements BreedDao {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<String> getBreedByPetTypeByBreed(String petType, String breed) {
        List<String> listBreed = new ArrayList<>();
        listBreed.addAll(
                manager.createNativeQuery("SELECT breed from pet_breed where " +
                                "pet_type % :pt and breed % :br")
                        .setParameter("pt", petType)
                        .setParameter("br", breed)
                        .getResultList());
        return listBreed;
    }

    @Override
    public List<String> getBreedByBreed(String breed) {
        return manager.createNativeQuery("SELECT breed from pet_breed where breed % :br ")
                        .setParameter("br", breed).getResultList();
    }

    @Override
    public List<String> getAllBreeds() {
        return manager.createNativeQuery("SELECT breed FROM pet_breed").getResultList();
    }

    @Override
    public List<String> getBreedsByPetType(String petType) {
        return manager.createNativeQuery("SELECT breed from pet_breed where pet_type % :pt ")
                .setParameter("pt", petType).getResultList();
    }

    @Override
    public Boolean isPetTypeAndBreedCombinationExist(String petType, String breed) {
        return (Boolean) manager.createNativeQuery("SELECT EXISTS(SELECT pet_type, breed FROM pet_breed WHERE " +
                        "pet_type = :petType AND breed = :breed)")
                .setParameter("petType", petType).setParameter("breed", breed).getSingleResult();
    }

    @Override
    public void addBreeds(String petType, List<String> breeds) {
        final String sql = "INSERT INTO pet_breed (pet_type, breed) VALUES (:petType, :breed) ON CONFLICT DO NOTHING;";
        for(String breed : breeds) {
            manager.createNativeQuery(sql)
                    .setParameter("petType", petType)
                    .setParameter("breed", breed.trim()).executeUpdate();
        }
    }


    @Override
    public void deleteBreeds(String petType, List<String> breeds) {
        final String sql = "DELETE FROM pet_breed WHERE pet_type = :petType AND breed IN (:breeds);";
        manager.createNativeQuery(sql)
                .setParameter("petType", petType)
                .setParameter("breeds", breeds.stream().map(s -> s.toLowerCase().trim()).collect(Collectors.toList()))
                .executeUpdate();
    }
}
