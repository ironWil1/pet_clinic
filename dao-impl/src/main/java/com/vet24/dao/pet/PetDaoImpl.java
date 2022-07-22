package com.vet24.dao.pet;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.Pet;
import org.springframework.stereotype.Repository;

@Repository
public class PetDaoImpl extends ReadWriteDaoImpl<Long, Pet> implements PetDao {
    @Override
    public boolean isExistByPetIdAndClientId(Long petId, Long clientId) {
        String query = "SELECT CASE WHEN (count(*)>0) then true else false end" +
                " FROM Pet p WHERE p.id = :petId and p.client.id = :clientId";
        return manager
                .createQuery(query, Boolean.class)
                .setParameter("petId", petId)
                .setParameter("clientId", clientId)
                .getSingleResult();

    }
}
