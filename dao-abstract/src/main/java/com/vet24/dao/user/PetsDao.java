package com.vet24.dao.user;

import com.vet24.models.pet.Pet;

import java.util.List;

public interface PetsDao {
    List<Pet> getPetsOfCurrentUser(Long id);
}
