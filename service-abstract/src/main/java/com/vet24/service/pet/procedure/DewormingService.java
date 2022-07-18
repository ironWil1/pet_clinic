package com.vet24.service.pet.procedure;

import com.vet24.models.pet.procedure.Deworming;
import com.vet24.service.ReadWriteService;

import java.util.List;

public interface DewormingService extends ReadWriteService<Long, Deworming> {
    boolean isExistByDewormingIdAndClientId(Long dewormingId,Long clientId);
    List<Deworming> getByPetId(Long petId);
}
