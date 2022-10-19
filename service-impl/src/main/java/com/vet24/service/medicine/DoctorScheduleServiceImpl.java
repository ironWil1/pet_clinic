package com.vet24.service.medicine;

import com.vet24.dao.medicine.DoctorScheduleDao;
import com.vet24.models.medicine.DoctorSchedule;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    @Override
    public List<DoctorSchedule> getDoctorScheduleAfterDate(LocalDate date) {
        return doctorScheduleDao.getDoctorScheduleAfterDate(date);
    }
    @Override
    public String getDoctorScheduleWorkShift(Long doctorId, LocalDate date) {
        return doctorScheduleDao.getDoctorScheduleWorkShift(doctorId, date);
    }
    @Override
    public List<DoctorSchedule> getDoctorScheduleCurrentDate(LocalDate date) {
        return doctorScheduleDao.getDoctorScheduleCurrentDate(date);
    }

    @Override
    public Long getDoctorId(DoctorSchedule doctorSchedule) {
        return doctorScheduleDao.getDoctorId(doctorSchedule);
    }
}

