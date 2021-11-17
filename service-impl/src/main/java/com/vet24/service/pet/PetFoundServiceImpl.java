package com.vet24.service.pet;

import com.vet24.dao.pet.PetFoundDao;
import com.vet24.models.pet.PetFound;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PetFoundServiceImpl extends ReadWriteServiceImpl<Long, PetFound> implements PetFoundService {

    public PetFoundServiceImpl(PetFoundDao petFoundDao) {
        super(petFoundDao);
    }
}
