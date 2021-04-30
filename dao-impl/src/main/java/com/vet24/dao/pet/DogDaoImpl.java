package com.vet24.dao.pet;

import com.vet24.models.pet.Dog;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class DogDaoImpl implements DogDao {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public Dog findById(Long id) {
        return entityManager.find(Dog.class, id);
    }

    @Override
    public List<Dog> findAll() {
        return entityManager.createQuery("select d from Dog d", Dog.class).getResultList();
    }

    @Override
    public void save(Dog dog) {
        entityManager.persist(dog);
    }

    @Override
    public void update(Dog dog) {
        entityManager.merge(dog);
    }

    @Override
    public void delete(Long id) {
        Dog dog = entityManager.find(Dog.class, id);
        if (dog != null) {
            entityManager.remove(dog);
        }
    }
}
