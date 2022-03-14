package com.vet24.service.user;

import com.vet24.models.user.Doctor;
import com.vet24.models.user.DoctorNonWorking;
import com.vet24.service.ReadWriteService;

import java.time.LocalDate;
import java.util.List;

public interface DoctorNonWorkingService extends ReadWriteService<Long, DoctorNonWorking> {

    boolean isExistByDoctorIdAndDate(Doctor doctor, LocalDate date);
    List<DoctorNonWorking> getDoctorNonWorkingAfterDate(LocalDate date);

}
