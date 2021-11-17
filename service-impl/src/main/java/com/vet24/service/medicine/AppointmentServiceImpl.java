package com.vet24.service.medicine;

import com.vet24.dao.medicine.AppointmentDao;
import com.vet24.models.medicine.Appointment;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AppointmentServiceImpl extends ReadWriteServiceImpl<Long, Appointment> implements AppointmentService {

    protected AppointmentServiceImpl(AppointmentDao appointmentDao) {
        super(appointmentDao);
    }
}
