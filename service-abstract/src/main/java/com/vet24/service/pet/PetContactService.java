package com.vet24.service.pet;

import com.vet24.models.pet.PetContact;
import com.vet24.service.ReadWriteService;

import java.util.List;

public interface PetContactService extends ReadWriteService<Long, PetContact> {

    List<String> getAllUniqueCode();
    int getCountId();
    String randomUniqueCode();
}
