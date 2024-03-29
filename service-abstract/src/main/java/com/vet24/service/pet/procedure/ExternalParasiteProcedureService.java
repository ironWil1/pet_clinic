package com.vet24.service.pet.procedure;

import com.vet24.models.pet.procedure.ExternalParasiteProcedure;
import com.vet24.service.ReadWriteService;

import java.util.List;

public interface ExternalParasiteProcedureService extends ReadWriteService<Long, ExternalParasiteProcedure> {
    boolean isExistByIdAndClientId(Long id,Long clientId);

    List<ExternalParasiteProcedure> getByPetId(Long petId);
}
