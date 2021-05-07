package com.vet24.service.pet;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.dao.pet.PetContactDao;
import com.vet24.models.pet.PetContact;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetContactServiceImpl extends ReadWriteServiceImpl<Long, PetContact> implements PetContactService {

    @Autowired
    private PetContactDao petContactDao;

    protected PetContactServiceImpl(ReadWriteDaoImpl<Long, PetContact> readWriteDao) {
        super(readWriteDao);
    }
}
