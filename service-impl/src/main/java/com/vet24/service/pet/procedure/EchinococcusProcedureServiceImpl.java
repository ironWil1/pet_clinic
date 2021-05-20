package com.vet24.service.pet.procedure;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.dao.pet.procedure.EchinococcusProcedureDao;
import com.vet24.models.pet.procedure.EchinococcusProcedure;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EchinococcusProcedureServiceImpl extends ReadWriteServiceImpl<Long, EchinococcusProcedure> implements EchinococcusProcedureService {

    private final EchinococcusProcedureDao echinococcusProcedureDao;

    @Autowired
    public EchinococcusProcedureServiceImpl(ReadWriteDaoImpl<Long, EchinococcusProcedure> readWriteDao, EchinococcusProcedureDao echinococcusProcedureDao) {
        super(readWriteDao);
        this.echinococcusProcedureDao = echinococcusProcedureDao;
    }
}
