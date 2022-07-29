package com.vet24.service.pet.procedure;

import com.vet24.dao.pet.procedure.ExternalParasiteProcedureDao;
import com.vet24.models.pet.procedure.ExternalParasiteProcedure;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExternalParasiteProcedureServiceImpl extends ReadWriteServiceImpl<Long, ExternalParasiteProcedure> implements ExternalParasiteProcedureService {

    private final ExternalParasiteProcedureDao externalParasiteProcedureDao;

    @Autowired
    public ExternalParasiteProcedureServiceImpl(ExternalParasiteProcedureDao externalParasiteProcedureDao) {
        super(externalParasiteProcedureDao);
        this.externalParasiteProcedureDao = externalParasiteProcedureDao;
    }

    @Override
    public boolean isExistByIdAndClientId(Long id, Long clientId) {
        return externalParasiteProcedureDao.isExistByIdAndClientId(id, clientId);
    }
}
