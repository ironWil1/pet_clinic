package com.vet24.dao.pet;

import com.vet24.models.pet.Pet;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PetDaoImpl implements PetDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Pet findById(Long id) {
        return entityManager.find(Pet.class, id);
    }

    @Override
    public List<Pet> findAll() {
        return entityManager.createQuery("select p from Pet p", Pet.class).getResultList();
    }

    @Override
    public void save(Pet pet) {
        entityManager.persist(pet);
    }

    @Override
    public void update(Pet pet) {
        entityManager.merge(pet);
    }

    @Override
    public void delete(Long id) {
        Pet pet = entityManager.find(Pet.class, id);
        if (pet != null) {
            entityManager.remove(pet);
        }
    }
}
