package com.vet24.dao.pet;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.Pet;
import org.springframework.stereotype.Repository;

@Repository
public class PetDaoImpl extends ReadWriteDaoImpl<Long, Pet> implements PetDao {
    @Override
    public Boolean isPetBelongToClientByPetId(Long petId, Long clientId) {
        return manager
                .createQuery("SELECT CASE WHEN (COUNT (*)>0) THEN TRUE ELSE FALSE END" +
                        " FROM Pet pet WHERE pet.id =:petId AND pet.client.id =: clientId", Boolean.class)
                .setParameter("petId", petId)
                .setParameter("clientId", clientId)
                .getSingleResult();
    }
}
