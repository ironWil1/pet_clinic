package com.vet24.service.pet.procedure;

import com.vet24.dao.ReadOnlyDaoImpl;
import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.procedure.Procedure;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcedureServiceImpl extends ReadWriteServiceImpl<Procedure, Long> {

    @Autowired
    protected ProcedureServiceImpl(ReadOnlyDaoImpl<Procedure, Long> readOnlyDao, ReadWriteDaoImpl<Procedure, Long> readWriteDao) {
        super(readOnlyDao, readWriteDao);
    }
}
