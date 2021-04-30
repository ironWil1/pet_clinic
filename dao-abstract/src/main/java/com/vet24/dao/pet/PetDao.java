package com.vet24.dao.pet;

import com.vet24.models.pet.Pet;

import java.util.List;

public interface PetDao {

    Pet findById(Long id);

    List<Pet> findAll();

    void save(Pet pet);

    void update(Pet pet);

    void delete(Long id);
}
