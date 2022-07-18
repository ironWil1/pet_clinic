package com.vet24.service.pet.procedure;

import com.vet24.dao.pet.procedure.DewormingDao;
import com.vet24.models.pet.procedure.Deworming;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DewormingServiceImpl extends ReadWriteServiceImpl<Long, Deworming> implements DewormingService {

    private final DewormingDao dewormingDao;

    @Autowired
    public DewormingServiceImpl(DewormingDao dewormingDao) {
        super(dewormingDao);
        this.dewormingDao = dewormingDao;
    }
}
