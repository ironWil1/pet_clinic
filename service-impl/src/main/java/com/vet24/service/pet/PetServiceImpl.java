package com.vet24.service.pet;

import com.vet24.dao.pet.PetDao;
import com.vet24.models.pet.Pet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PetServiceImpl implements PetService {

    private final PetDao petDao;

    public PetServiceImpl(PetDao petDao) {
        this.petDao = petDao;
    }


    @Transactional(readOnly = true)
    @Override
    public Pet findById(Long id) {
        return petDao.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Pet> findAll() {
        return petDao.findAll();
    }

    @Transactional
    @Override
    public void save(Pet pet) {
        petDao.save(pet);
    }

    @Transactional
    @Override
    public void update(Pet pet) {
        petDao.update(pet);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        petDao.delete(id);
    }
}
