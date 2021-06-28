package com.vet24.dao.pet;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.pet.Pet;
import org.springframework.data.jpa.repository.Query;

public interface PetDao extends ReadWriteDao<Long, Pet> {

    @Query("select p from Pet p where p.id =:petId")
    Long getPetId(Long petId);
}
