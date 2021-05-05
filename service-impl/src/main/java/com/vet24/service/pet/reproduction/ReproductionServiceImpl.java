package com.vet24.service.pet.reproduction;

import com.vet24.dao.ReadOnlyDaoImpl;
import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.reproduction.Reproduction;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReproductionServiceImpl extends ReadWriteServiceImpl<Reproduction, Long> {

    @Autowired
    protected ReproductionServiceImpl(ReadOnlyDaoImpl<Reproduction, Long> readOnlyDao, ReadWriteDaoImpl<Reproduction, Long> readWriteDao) {
        super(readOnlyDao, readWriteDao);
    }
}
