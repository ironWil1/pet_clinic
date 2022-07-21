package com.vet24.dao.pet.procedure;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.pet.procedure.Deworming;

import java.util.List;

public interface DewormingDao extends ReadWriteDao<Long, Deworming> {
    boolean isExistByDewormingIdAndClientId(Long dewormingId,Long clientId);
    List<Deworming> getByPetId(Long petId);
}
