package com.vet24.service.pet;

import com.vet24.models.pet.PetFound;
import com.vet24.service.ReadWriteService;

import java.util.List;

public interface PetFoundService extends ReadWriteService<Long, PetFound> {

    List<PetFound> getPetFoundByPetId(Long petId);

    boolean isExistByPetId(Long petId);
}
