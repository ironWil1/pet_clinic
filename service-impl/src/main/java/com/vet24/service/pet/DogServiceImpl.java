package com.vet24.service.pet;

import com.vet24.dao.pet.DogDao;
import com.vet24.models.pet.Dog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DogServiceImpl implements DogService {

    private final DogDao dogDao;

    public DogServiceImpl(DogDao dogDao) {
        this.dogDao = dogDao;
    }


    @Transactional(readOnly = true)
    @Override
    public Dog findById(Long id) {
        return dogDao.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Dog> findAll() {
        return dogDao.findAll();
    }

    @Transactional
    @Override
    public void save(Dog dog) {
        dogDao.save(dog);
    }

    @Transactional
    @Override
    public void update(Dog dog) {
        dogDao.update(dog);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        dogDao.delete(id);
    }
}
