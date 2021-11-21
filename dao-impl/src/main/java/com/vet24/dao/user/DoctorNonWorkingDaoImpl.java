package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.Doctor;
import com.vet24.models.user.DoctorNonWorking;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.time.LocalDate;

@Repository
public class DoctorNonWorkingDaoImpl extends ReadWriteDaoImpl<Long, DoctorNonWorking> implements DoctorNonWorkingDao {

    @Override
    public boolean isExistByDoctorIdAndDate(Doctor doctor, LocalDate date) {
        try {
            Long id = doctor.getId();
            Long dnw = manager.createQuery(
                            "SELECT d.doctor.id FROM DoctorNonWorking d WHERE d.doctor.id = :id AND d.date = :date", Long.class)
                    .setParameter("id", id)
                    .setParameter("date", date)
                    .getSingleResult();
            return dnw.equals(id);
        } catch(NoResultException e) {
            e.getStackTrace();
        }
        return false;
    }
}