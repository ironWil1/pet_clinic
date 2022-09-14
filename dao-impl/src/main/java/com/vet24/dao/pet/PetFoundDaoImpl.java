package com.vet24.dao.pet;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.PetFound;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PetFoundDaoImpl extends ReadWriteDaoImpl<Long, PetFound> implements PetFoundDao {

    @Override
    public List<PetFound> getPetFoundByPetId(Long petId) {
        return manager.createQuery("from PetFound pf where pf.pet.id = :petId order by pf.foundDate")
                .setParameter("petId", petId)
                .getResultList();
    }

    @Override
    public boolean isExistByPetId(Long petId) {
        return manager.createQuery("SELECT CASE WHEN (count(*)>0) then true else false end" +
                                           " from PetFound pf where pf.pet.id = :petId", Boolean.class)
                .setParameter("petId", petId)
                .getSingleResult();
    }
}
