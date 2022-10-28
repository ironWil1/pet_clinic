package com.vet24.service.pet.appearance;

import com.vet24.dao.pet.appearance.BreedDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return breedDao.getBreedByPetTypeByBreed(petType, breed);
    }

    @Override
    public List<String> getBreedByBreed(String breed) {
        return breedDao.getBreedByBreed(breed);
    }

    @Override
    public List<String> getBreedsByPetType(String petType) {
        return breedDao.getBreedsByPetType(petType);
    }

    @Override
    public List<String> getAllBreeds() {
        return breedDao.getAllBreeds();
    }

    @Override
    public Boolean isBreedExists(String petType, String breed) {
        return breedDao.isPetTypeAndBreedCombinationExist(petType, breed);
    }

    @Transactional
    @Override
    public void addBreeds(String petType, List<String> breeds) {
        breedDao.addBreeds(petType, breeds);
    }

    @Transactional
    @Override
    public void deleteBreeds(String petType, List<String> breeds) {
        breedDao.deleteBreeds(petType, breeds);
    }
}
