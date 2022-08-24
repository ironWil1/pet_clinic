package com.vet24.service.pet;

import com.vet24.dao.pet.PetFoundDao;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.PetFound;
import com.vet24.models.user.User;
import com.vet24.service.ReadWriteServiceImpl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetFoundServiceImpl extends ReadWriteServiceImpl<Long, PetFound> implements PetFoundService {

    private final PetFoundDao petFoundDao;

    public PetFoundServiceImpl(PetFoundDao petFoundDao) {
        super(petFoundDao);
        this.petFoundDao = petFoundDao;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return petFoundDao.findByLogin(login);
    }

    @Override
    public List<Pet> getClientPet(Long petId, Long userId) {
        return petFoundDao.getClientPet(petId, userId);
    }

    @Override
    public List<PetFound> getPetFoundById(Long petId) {
        return petFoundDao.getPetFoundById(petId);
    }
}
