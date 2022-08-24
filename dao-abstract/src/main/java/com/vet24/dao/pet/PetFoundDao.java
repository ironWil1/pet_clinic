package com.vet24.dao.pet;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.PetFound;
import com.vet24.models.user.User;

import java.util.List;
import java.util.Optional;

public interface PetFoundDao extends ReadWriteDao<Long, PetFound> {

    Optional<User> findByLogin(String login);

    List<Pet> getClientPet(Long petId, Long userId);

    List<PetFound> getPetFoundById(Long petId);
}
