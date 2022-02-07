package com.vet24.service.pet.procedure;

import com.vet24.dao.pet.procedure.ProcedureDao;
import com.vet24.models.pet.procedure.Procedure;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProcedureServiceImpl extends ReadWriteServiceImpl<Long, Procedure> implements ProcedureService {

    private final ProcedureDao procedureDao;

    @Autowired
    public ProcedureServiceImpl(ProcedureDao procedureDao) {
        super(procedureDao);
        this.procedureDao = procedureDao;
    }

    @Override
    public void persist(Procedure procedure) {
        procedureDao.persist(procedure);
    }

    @Override
    public Procedure update(Procedure procedure) {
        return procedureDao.update(procedure);
    }

    @Override
    public void delete(Procedure procedure) {
        procedureDao.delete(procedure);
    }

}