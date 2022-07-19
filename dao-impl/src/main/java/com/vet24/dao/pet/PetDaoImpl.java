package com.vet24.dao.pet;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.Pet;
import org.springframework.stereotype.Repository;

@Repository
public class PetDaoImpl extends ReadWriteDaoImpl<Long, Pet> implements PetDao {
    @Override
    public boolean isPetBelongToClient(Long petId, Long clientId) {
        return manager
                .createQuery("SELECT CASE WHEN (count(*)>0) then true else false end FROM Pet WHERE id = :id AND client_id = :client_id", Boolean.class)
                .setParameter("id", petId)
                .setParameter("client_id", clientId)
                .getSingleResult();
    }
}
