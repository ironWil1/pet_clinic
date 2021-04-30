package com.vet24.dao.pet;

import com.vet24.models.pet.Dog;

import java.util.List;

public interface DogDao {

    Dog findById(Long id);

    List<Dog> findAll();

    void save(Dog dog);

    void update(Dog dog);

    void delete(Long id);
}
