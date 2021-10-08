package com.vet24.dao.medicine;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.medicine.DoctorSchedule;
import org.springframework.stereotype.Repository;

@Repository
public class DoctorScheduleDaoImpl extends ReadWriteDaoImpl<Long, DoctorSchedule> implements DoctorScheduleDao {
}
