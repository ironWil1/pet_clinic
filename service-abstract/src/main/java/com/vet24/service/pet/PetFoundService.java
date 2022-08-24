package com.vet24.service.pet;

import com.vet24.models.pet.Pet;
import com.vet24.models.pet.PetFound;
import com.vet24.models.user.User;
import com.vet24.service.ReadWriteService;

import java.util.List;
import java.util.Optional;

public interface PetFoundService extends ReadWriteService<Long, PetFound> {

    Optional<User> findByLogin(String login);

    List<Pet> getClientPet(Long petId, Long userId);

    List<PetFound> getPetFoundById(Long petId);
}
