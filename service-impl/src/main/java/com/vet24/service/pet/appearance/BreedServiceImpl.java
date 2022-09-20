package com.vet24.service.pet.appearance;

import com.vet24.dao.pet.appearance.BreedDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BreedServiceImpl implements BreedService {

    private final BreedDao breedDao;

    @Autowired
    public BreedServiceImpl(BreedDao breedDao) {
        this.breedDao = breedDao;
    }

    @Override
    public List<String> getBreed(String petType, String breed) {
        List<String> breedList;
        if (petType.isEmpty()) {
            breedList = breedDao.getBreedByBreed(breed);
        } else {
            breedList = breedDao.getBreedByPetTypeByBreed(petType, breed);
        }
        return breedList;
    }

    @Override
    public Boolean isBreedExists(String petType, String breed) {
        return breedDao.isPetTypeAndBreedCombinationExist(petType, breed);
    }

}
