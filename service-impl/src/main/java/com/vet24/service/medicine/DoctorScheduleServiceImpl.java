package com.vet24.service.medicine;

import com.vet24.dao.medicine.DoctorScheduleDao;
import com.vet24.models.medicine.DoctorSchedule;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DoctorScheduleServiceImpl extends ReadWriteServiceImpl<Long, DoctorSchedule> implements DoctorScheduleService {
    private final DoctorScheduleDao doctorScheduleDao;

    @Autowired
    public DoctorScheduleServiceImpl(DoctorScheduleDao doctorScheduleDao) {
        super(doctorScheduleDao);
        this.doctorScheduleDao = doctorScheduleDao;
    }

    @Override
    public boolean isExistByDoctorIdAndWeekNumber(Long doctorId, LocalDate startWeek) {
        return doctorScheduleDao.isExistByDoctorIdAndWeekNumber(doctorId, startWeek);
    }
}

