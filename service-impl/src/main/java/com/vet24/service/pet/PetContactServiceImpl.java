package com.vet24.service.pet;

import com.vet24.dao.pet.PetContactDao;
import com.vet24.models.pet.Pet;
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
    public List<String> getAllCode() {
        return petContactDao.getAllCode();
    }

    @Override
    public boolean isExistByCode(String code) {
        return petContactDao.isExistByCode(code);
    }

    @Override
    public PetContact getByCode(String code) {
        return petContactDao.getByCode(code);
    }

    @Override
    public int getCountId() {
        return petContactDao.getCountId();
    }

    @Override
    public String randomPetContactUniqueCode() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String unchangedCode(PetContact petContact) {
        return petContact.getCode();
    }

    @Override
    public PetContact getByPet(Pet pet) {
        return petContactDao.getByPet(pet);
    }
}
