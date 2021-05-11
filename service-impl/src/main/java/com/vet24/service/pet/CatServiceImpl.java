package com.vet24.service.pet;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.dao.pet.CatDao;
import com.vet24.dao.user.ClientDao;
import com.vet24.models.pet.Cat;
import com.vet24.models.user.Client;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CatServiceImpl extends ReadWriteServiceImpl<Long, Cat> implements CatService {

    private final CatDao catDao;

    public CatServiceImpl(ReadWriteDaoImpl<Long, Cat> readWriteDao, CatDao catDao) {
        super(readWriteDao);
        this.catDao = catDao;
    }
}
