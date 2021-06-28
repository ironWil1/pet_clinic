package com.vet24.dao.pet;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.Pet;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class PetDaoImpl extends ReadWriteDaoImpl<Long, Pet> implements PetDao {
    @Override
    public Pet getPetId(Long petId) {

        try {
            return manager.createQuery("SELECT p FROM Pet p WHERE p.id =:petId", Pet.class)
                    .setParameter("petId", petId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
