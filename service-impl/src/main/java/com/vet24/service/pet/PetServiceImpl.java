package com.vet24.service.pet;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.dao.pet.PetDao;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.PetContact;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetServiceImpl extends ReadWriteServiceImpl<Long, Pet> implements PetService {

    @Autowired
    private PetDao petDao;

    protected PetServiceImpl(ReadWriteDaoImpl<Long, Pet> readWriteDao) {
        super(readWriteDao);
    }
}
