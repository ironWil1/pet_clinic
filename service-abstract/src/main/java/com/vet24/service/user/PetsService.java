package com.vet24.service.user;

import com.vet24.models.pet.Pet;

import java.util.List;

public interface PetsService {

    List<Pet> getAllPetsOfUser(Long id);
}
