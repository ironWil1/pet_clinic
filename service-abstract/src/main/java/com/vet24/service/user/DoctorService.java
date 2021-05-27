package com.vet24.service.user;


import com.vet24.models.user.Doctor;
import com.vet24.service.ReadWriteService;

import java.util.List;

public interface DoctorService extends ReadWriteService<Long, Doctor> {

    Doctor getOne(Long id);
    
    void persist(Doctor doctor);

    void persistAll(List<Doctor> entities);
}
