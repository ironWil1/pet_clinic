package com.vet24.service.user;

import com.vet24.dao.user.DoctorDao;
import com.vet24.models.user.Client;
import com.vet24.models.user.Doctor;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DoctorServiceImpl extends ReadWriteServiceImpl<Long, Doctor> implements DoctorService{

    private final DoctorDao doctorDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DoctorServiceImpl(DoctorDao  doctorDao, PasswordEncoder passwordEncoder) {
        super(doctorDao);
        this.doctorDao = doctorDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional()
    public void persist(Doctor doctor) {
        String password = passwordEncoder.encode(doctor.getPassword());
        doctor.setPassword(password);
        doctorDao.persist(doctor);
    }

    @Override
    @Transactional
    public Doctor update(Doctor doctor) {
        String newPassword = doctor.getPassword();
        if(passwordEncoder.upgradeEncoding(newPassword)) {
            String password = passwordEncoder.encode(newPassword);
            doctor.setPassword(password);
        }
        return doctorDao.update(doctor);
    }
}
