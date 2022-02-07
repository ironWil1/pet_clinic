package com.vet24.service.medicine;

import com.vet24.dao.medicine.DoctorScheduleDao;
import com.vet24.models.medicine.DoctorSchedule;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public boolean isExistByDoctorIdAndWeekNumber(Long doctorId, Integer weekNumber) {
        return doctorScheduleDao.isExistByDoctorIdAndWeekNumber(doctorId, weekNumber);
    }

    @Override
    public List<DoctorSchedule> getDoctorScheduleListAfterWeekNumber(Integer weekNumber) {
        return doctorScheduleDao.getDoctorScheduleListAfterWeekNumber(weekNumber);
    }


}

