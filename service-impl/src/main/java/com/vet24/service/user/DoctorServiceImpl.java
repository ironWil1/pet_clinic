package com.vet24.service.user;

import com.vet24.dao.user.DoctorDao;
import com.vet24.models.user.Doctor;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DoctorServiceImpl extends ReadWriteServiceImpl<Long, Doctor> implements DoctorService {

    private final DoctorDao doctorDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DoctorServiceImpl(DoctorDao doctorDao, PasswordEncoder passwordEncoder) {
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
        if (passwordEncoder.upgradeEncoding(newPassword)) {
            String password = passwordEncoder.encode(newPassword);
            doctor.setPassword(password);
        }
        return doctorDao.update(doctor);
    }

    @Override
    @Transactional
    public void persistAll(List<Doctor> doctors) {
        for (Doctor doctor : doctors) {
            String password = passwordEncoder.encode(doctor.getPassword());
            doctor.setPassword(password);
        }
        doctorDao.persistAll(doctors);
    }

    @Override
    @Transactional
    public List<Doctor> updateAll(List<Doctor> doctors) {
        for (Doctor doctor : doctors) {
            String newPassword = doctor.getPassword();
            if (passwordEncoder.upgradeEncoding(newPassword)) {
                String password = passwordEncoder.encode(newPassword);
                doctor.setPassword(password);
            }
        }
        return doctorDao.updateAll(doctors);
    }
}
