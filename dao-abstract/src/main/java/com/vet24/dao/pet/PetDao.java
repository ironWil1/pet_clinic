package com.vet24.dao.pet;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.pet.Pet;


public interface PetDao extends ReadWriteDao<Long, Pet> {
    Boolean isPetBelongToClientByPetId(Long petId, Long clientId);
}
