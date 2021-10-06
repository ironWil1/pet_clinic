package com.vet24.service.user;

import com.vet24.dao.user.DoctorNonWorkingDao;
import com.vet24.models.user.DoctorNonWorking;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorNonWorkingServiceImpl extends ReadWriteServiceImpl<Long, DoctorNonWorking> implements DoctorNonWorkingService {

    @Autowired
    public DoctorNonWorkingServiceImpl(DoctorNonWorkingDao readWriteDao) {
        super(readWriteDao);
    }
}
