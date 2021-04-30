package com.vet24.service.pet;

import com.vet24.models.pet.Pet;

import java.util.List;

public interface PetService {

    Pet findById(Long id);

    List<Pet> findAll();

    void save(Pet pet);

    void update(Pet pet);

    void delete(Long id);
}
