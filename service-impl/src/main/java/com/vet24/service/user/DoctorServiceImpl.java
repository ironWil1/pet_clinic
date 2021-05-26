package com.vet24.service.user;


import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.dao.user.DoctorDao;
import com.vet24.models.user.Doctor;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorServiceImpl extends ReadWriteServiceImpl<Long, Doctor> implements DoctorService{

    private final DoctorDao doctorDao;

    @Autowired
    public DoctorServiceImpl(ReadWriteDaoImpl<Long, Doctor> readWriteDao,
                             DoctorDao  doctorDao) {
        super(readWriteDao);
        this.doctorDao = doctorDao;

    }

    public Doctor getOne(Long id){
        return doctorDao.getByKey(id);
    }

    public void persist(Doctor doctor){
         doctorDao.persist(doctor);
    }
}
