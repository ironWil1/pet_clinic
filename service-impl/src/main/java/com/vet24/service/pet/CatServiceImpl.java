package com.vet24.service.pet;

import com.vet24.dao.pet.CatDao;
import com.vet24.models.pet.Cat;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatServiceImpl extends ReadWriteServiceImpl<Long, Cat>
        implements CatService {

    @Autowired
    public CatServiceImpl( CatDao catDao) {
        super(catDao);
    }
}
