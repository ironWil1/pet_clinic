package com.vet24.service.user;


import com.vet24.models.user.Doctor;

public interface DoctorService {

    public Doctor getOne(Long id);

    public void persist(Doctor doctor);
}
