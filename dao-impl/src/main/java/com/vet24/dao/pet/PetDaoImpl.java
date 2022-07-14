package com.vet24.dao.pet;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.Pet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.webjars.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Slf4j
public class PetDaoImpl extends ReadWriteDaoImpl<Long, Pet> implements PetDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Boolean isPetBelongToClientByPetId(Long petId, Long clientId) {
        if (petId == null) {
            log.info("Питомец с id {} не найден", petId);
            throw new NotFoundException("Питомец не найден");
        }

        return entityManager.createQuery("FROM Pet where id =:petId", Pet.class)
                .setParameter("petId", petId)
                .getSingleResult()
                .getClient().getId()
                .equals(clientId);
    }
}
