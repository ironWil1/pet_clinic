package com.vet24.service.pet.reproduction;

import com.vet24.models.pet.reproduction.Reproduction;
import com.vet24.service.ReadWriteService;

import java.util.List;

public interface ReproductionService extends ReadWriteService<Long, Reproduction> {
    List<Reproduction> getAllByPetId(Long petId);
}
