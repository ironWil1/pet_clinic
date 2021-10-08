package com.vet24.dao.medicine;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.medicine.Appointment;
import org.springframework.stereotype.Repository;

@Repository
public class AppointmentDaoImpl extends ReadWriteDaoImpl<Long, Appointment> implements AppointmentDao {
}
