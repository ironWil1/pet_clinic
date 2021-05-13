package com.vet24.service.pet.procedure;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.dao.pet.procedure.ProcedureDao;
import com.vet24.models.pet.procedure.Procedure;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcedureServiceImpl extends ReadWriteServiceImpl<Long, Procedure> implements ProcedureService {

    private final ProcedureDao procedureDao;

    @Autowired
    public ProcedureServiceImpl(ReadWriteDaoImpl<Long, Procedure> readWriteDao, ProcedureDao procedureDao) {
        super(readWriteDao);
        this.procedureDao = procedureDao;
    }
}
