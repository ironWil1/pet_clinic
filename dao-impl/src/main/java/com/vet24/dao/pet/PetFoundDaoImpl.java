package com.vet24.dao.pet;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.PetFound;
import org.springframework.stereotype.Repository;

@Repository
public class PetFoundDaoImpl extends ReadWriteDaoImpl<Long, PetFound> implements PetFoundDao {

}
