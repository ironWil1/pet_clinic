package com.vet24.service.user;

import com.vet24.dao.user.DoctorNonWorkingDao;
import com.vet24.models.user.DoctorNonWorking;
import com.vet24.models.user.User;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DoctorNonWorkingServiceImpl extends ReadWriteServiceImpl<Long, DoctorNonWorking> implements DoctorNonWorkingService {

    private final DoctorNonWorkingDao doctorNonWorkingDao;

    @Autowired
    public DoctorNonWorkingServiceImpl(DoctorNonWorkingDao readWriteDao, DoctorNonWorkingDao doctorNonWorkingDao) {
        super(readWriteDao);
        this.doctorNonWorkingDao = doctorNonWorkingDao;
    }

    public boolean isExistByDoctorIdAndDate(User doctor, LocalDate date) {
        return doctorNonWorkingDao.isExistByDoctorIdAndDate(doctor, date);
    }

    public List<DoctorNonWorking> getDoctorNonWorkingAfterDate(LocalDate date) {
        return doctorNonWorkingDao.getDoctorNonWorkingAfterDate(date);
    }

    @Override
    public List<LocalDate> getNonWorkingDatesByDoctorIdAndBetweenDates(Long doctorId, LocalDate dateStart, LocalDate dateEnd) {
        return doctorNonWorkingDao.getNonWorkingDatesByDoctorIdAndBetweenDates(doctorId, dateStart, dateEnd);
    }
}
