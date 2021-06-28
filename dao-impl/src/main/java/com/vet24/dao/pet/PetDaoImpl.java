package com.vet24.dao.pet;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.Pet;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class PetDaoImpl extends ReadWriteDaoImpl<Long, Pet> implements PetDao {

    @Override
    public Long getPetId(Long petId) {
        return petId;
    }
}
