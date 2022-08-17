package com.vet24.dao.pet.reproduction;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.pet.reproduction.Reproduction;

import java.util.List;

public interface ReproductionDao extends ReadWriteDao<Long, Reproduction> {
    List<Reproduction> getAllByPetId(Long petId);
}
