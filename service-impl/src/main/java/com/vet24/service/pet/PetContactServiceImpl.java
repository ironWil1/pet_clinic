package com.vet24.service.pet;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.dao.pet.PetContactDao;
import com.vet24.models.pet.PetContact;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
public class PetContactServiceImpl extends ReadWriteServiceImpl<Long, PetContact> implements PetContactService {

    static final String SYMBOLS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static final int LENGTH = 25;
    static SecureRandom random = new SecureRandom();

    private final PetContactDao petContactDao;

    public PetContactServiceImpl(ReadWriteDaoImpl<Long, PetContact> readWriteDao, PetContactDao petContactDao) {
        super(readWriteDao);
        this.petContactDao = petContactDao;
    }

    @Override
    public List<String> getAllUniqueCode() {
        return petContactDao.getAllUniqueCode();
    }

    @Override
    public int getCountId() {
        return petContactDao.getCountId();
    }

    @Override
    public String randomUniqueCode(){
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            sb.append(SYMBOLS.charAt( random.nextInt(SYMBOLS.length())));
        }
        //проверка по ид
        /*int countId = getCountId();
        for (long i = 1L; i < countId; i++) {
            PetContact pet = petContactDao.getByKey(i);
            if (pet.getUniqCode().equals(sb.toString())) {
                for (int j = 0; j < 3; j++) {
                    sb.deleteCharAt(random.nextInt(20));
                }
                for (int k = 0; k < 3; k++) {
                    sb.append(SYMBOLS.charAt( random.nextInt(SYMBOLS.length())));
                }
            }
        }*/
        // проверка по уникальному номеру
        List<String> allUniqueCode = petContactDao.getAllUniqueCode();
        for(String uniqueCode : allUniqueCode) {
            if (uniqueCode.equals(sb.toString())) {
                for (int j = 1; j <= 3; j++) {
                    sb.deleteCharAt(random.nextInt(20));
                }
                for (int k = 1; k <= 3; k++) {
                    sb.append(SYMBOLS.charAt( random.nextInt(SYMBOLS.length())));
                }
            }
        }
        return sb.toString();
    }
}
