package com.vet24.service.pet;

import com.vet24.dao.pet.PetContactDao;
import com.vet24.models.pet.PetContact;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PetContactServiceImpl extends ReadWriteServiceImpl<Long, PetContact> implements PetContactService {

    private final PetContactDao petContactDao;

    public PetContactServiceImpl(PetContactDao petContactDao) {
        super(petContactDao);
        this.petContactDao = petContactDao;
    }

    @Override
    public List<String> getAllPetCode() {
        return petContactDao.getAllPetCode();
    }

    @Override
    public boolean isExistByPetCode(String petCode) {
        return petContactDao.isExistByPetCode(petCode);
    }

    @Override
    public PetContact getByPetCode(String petCode) {
        return petContactDao.getByPetCode(petCode);
    }

    @Override
    public int getCountId() {
        return petContactDao.getCountId();
    }

    @Override
    public String randomPetContactUniqueCode() {
        return UUID.randomUUID().toString();
    }
}
