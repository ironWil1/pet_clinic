package com.vet24.dao.user;

import com.vet24.models.pet.Pet;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;



@Repository
public class PetsDaoImpl implements PetsDao {

    @PersistenceContext
    private EntityManager manager;

    @Override
    @SuppressWarnings("unchecked")
    public List<Pet> getPetsOfCurrentUser(Long id) {
        return manager.createNativeQuery("SELECT * FROM pet_entities WHERE client_id = :id", Pet.class)
                .setParameter("id", id).getResultList();
    }
}
