package com.vet24.dao.pet;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.PetFound;
import com.vet24.models.user.User;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PetFoundDaoImpl extends ReadWriteDaoImpl<Long, PetFound> implements PetFoundDao {

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.of((User) manager.createQuery("from User u where u.email = :login")
                .setParameter("login", login)
                .getSingleResult());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Pet> getClientPet(Long petId, Long userId) {
        return manager.createQuery("from Pet p where p.id = :petId and p.client.id = :id")
                .setParameter("petId", petId)
                .setParameter("id", userId)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PetFound> getPetFoundById(Long petId) {
        return manager.createQuery("from PetFound pf where pf.pet.id = :petId order by pf.foundDate")
                .setParameter("petId", petId)
                .getResultList();
    }
}
