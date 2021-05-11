package com.vet24.dao.pet;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.pet.PetContact;

import java.util.List;

public interface PetContactDao extends ReadWriteDao<Long, PetContact> {

    List<String> getAllUniqueCode();
    int getCountId();
}
