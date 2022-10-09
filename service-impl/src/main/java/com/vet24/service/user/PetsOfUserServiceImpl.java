package com.vet24.service.user;

import com.vet24.dao.user.PetsOfUserDao;
import com.vet24.models.pet.Pet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetsOfUserServiceImpl implements PetsOfUserService {

    private final PetsOfUserDao petsOfUserDao;

    public PetsOfUserServiceImpl(PetsOfUserDao petsOfUserDao) {
        this.petsOfUserDao = petsOfUserDao;
    }

    @Override
    public List<Pet> getAllPetsOfUser(Long id) {
        return petsOfUserDao.getPetsOfCurrentUser(id);
    }
}
