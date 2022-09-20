package com.vet24.service.user;

import com.vet24.dao.user.PetsDao;
import com.vet24.models.pet.Pet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetsServiceImpl implements PetsService {

    private final PetsDao petsDao;

    public PetsServiceImpl(PetsDao petsDao) {
        this.petsDao = petsDao;
    }

    @Override
    public List<Pet> getAllPetsOfUser(Long id) {
        return petsDao.getPetsOfCurrentUser(id);
    }
}
