package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.Doctor;
import com.vet24.models.user.DoctorNonWorking;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class DoctorNonWorkingDaoImpl extends ReadWriteDaoImpl<Long, DoctorNonWorking> implements DoctorNonWorkingDao {

    @Override
    public Long existDoctorEvent(Doctor doctor, LocalDate date) {
        /*
        SELECT id, date, type, doctor_id
        FROM doctor_non_working
        WHERE doctor_id = (doctor.getId) AND date = (date)
        preparedStatement
        чистый SQL, nativeSQL, HQL
         */
        DoctorNonWorking doctorNonWorking = new DoctorNonWorking();

        return doctorNonWorking.getId();
    }
}
