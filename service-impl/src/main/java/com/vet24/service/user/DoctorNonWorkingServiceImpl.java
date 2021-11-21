package com.vet24.service.user;

import com.vet24.dao.user.DoctorNonWorkingDao;
import com.vet24.models.user.Doctor;
import com.vet24.models.user.DoctorNonWorking;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DoctorNonWorkingServiceImpl extends ReadWriteServiceImpl<Long, DoctorNonWorking> implements DoctorNonWorkingService {

    private final DoctorNonWorkingDao doctorNonWorkingDao;

    @Autowired
    public DoctorNonWorkingServiceImpl(DoctorNonWorkingDao readWriteDao, DoctorNonWorkingDao doctorNonWorkingDao) {
        super(readWriteDao);
        this.doctorNonWorkingDao = doctorNonWorkingDao;
    }

    public boolean isExistByDoctorIdAndDate(Doctor doctor, LocalDate date) {
        return doctorNonWorkingDao.isExistByDoctorIdAndDate(doctor, date);
    }
}
