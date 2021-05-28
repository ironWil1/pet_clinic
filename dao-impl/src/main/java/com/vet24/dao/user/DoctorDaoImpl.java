package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.Doctor;
import org.springframework.stereotype.Repository;

@Repository
public class DoctorDaoImpl extends ReadWriteDaoImpl<Long, Doctor> implements DoctorDao {

}
