package com.vet24.dao.pet;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.pet.PetFound;

import java.util.List;

public interface PetFoundDao extends ReadWriteDao<Long, PetFound> {

    List<PetFound> getPetFoundByPetId(Long petId);
}
