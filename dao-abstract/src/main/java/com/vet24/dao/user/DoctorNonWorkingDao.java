package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.user.Doctor;
import com.vet24.models.user.DoctorNonWorking;

import java.time.LocalDate;

public interface DoctorNonWorkingDao extends ReadWriteDao<Long, DoctorNonWorking> {

    Long existDoctorEvent(Doctor doctor, LocalDate date);

}
