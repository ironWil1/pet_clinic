package com.vet24.service.user;

import com.vet24.models.user.Doctor;
import com.vet24.models.user.DoctorNonWorking;
import com.vet24.service.ReadWriteService;

import java.time.LocalDate;

public interface DoctorNonWorkingService extends ReadWriteService<Long, DoctorNonWorking> {

    Long isExistByDoctorIdAndDate(Doctor doctor, LocalDate date);

}
