package com.vet24.dao.pet.procedure;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.dto.pet.procedure.ExternalParasiteDto;
import com.vet24.models.pet.procedure.ExternalParasiteProcedure;

import java.util.List;

public interface ExternalParasiteProcedureDao extends ReadWriteDao<Long, ExternalParasiteProcedure> {
    boolean isExistByIdAndClientId(Long id, Long clientId);
}
