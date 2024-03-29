package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.DoctorNonWorking;
import com.vet24.models.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DoctorNonWorkingDaoImpl extends ReadWriteDaoImpl<Long, DoctorNonWorking> implements DoctorNonWorkingDao {

    @Override
    public boolean isExistByDoctorIdAndDate(User doctor, LocalDate date) {
        try {
            Long id = doctor.getId();
            Long dnw = manager.createQuery(
                            "SELECT d.doctor.id FROM DoctorNonWorking d WHERE d.doctor.id = :id AND d.date = :date", Long.class)
                    .setParameter("id", id)
                    .setParameter("date", date)
                    .getSingleResult();
            return dnw.equals(id);
        } catch (NoResultException e) {
            e.getStackTrace();
        }
        return false;
    }

    @Override
    public List<DoctorNonWorking> getDoctorNonWorkingAfterDate(LocalDate date) {
        return manager
                .createQuery("FROM DoctorNonWorking d WHERE d.date >= :date", DoctorNonWorking.class)
                .setParameter("date", date)
                .getResultList();
    }
    @Override
    public List<LocalDate> getNonWorkingDatesByDoctorIdAndBetweenDates(Long doctorId, LocalDate dateStart, LocalDate dateEnd) {
        return manager
                .createQuery("select d.date FROM DoctorNonWorking d WHERE d.doctor.id = :id AND d.date >= :dateStart AND d.date <= :dateEnd", LocalDate.class)
                .setParameter("id", doctorId)
                .setParameter("dateStart", dateStart)
                .setParameter("dateEnd", dateEnd)
                .getResultList();
    }
}