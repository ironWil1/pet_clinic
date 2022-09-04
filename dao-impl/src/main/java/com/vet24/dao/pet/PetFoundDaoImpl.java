package com.vet24.dao.pet;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.PetFound;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PetFoundDaoImpl extends ReadWriteDaoImpl<Long, PetFound> implements PetFoundDao {

    @Override
    @SuppressWarnings("unchecked")
    public List<PetFound> getPetFoundByPetId(Long petId) {
        return manager.createQuery("from PetFound pf where pf.pet.id = :petId order by pf.foundDate")
                .setParameter("petId", petId)
                .getResultList();
    }
}
