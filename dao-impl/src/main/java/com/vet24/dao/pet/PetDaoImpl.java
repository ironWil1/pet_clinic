package com.vet24.dao.pet;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.Pet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.webjars.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class PetDaoImpl extends ReadWriteDaoImpl<Long, Pet> implements PetDao {
    @Override
    public Boolean isPetBelongToClientByPetId(Long petId, Long clientId) {
        String query = "SELECT CASE WHEN (count(*)>0) then true else false end" +
                " FROM Pet pet where pet.id =:petId AND pet.client.id =: clientId";
        return manager
                .createQuery(query, Boolean.class)
                .setParameter("petId", petId)
                .setParameter("clientId", clientId)
                .getSingleResult();
    }
}
