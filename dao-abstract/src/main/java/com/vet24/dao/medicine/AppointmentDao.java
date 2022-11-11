package com.vet24.dao.medicine;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.medicine.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentDao extends ReadWriteDao<Long, Appointment> {

    List<LocalDateTime> getLocalDateTimeByDoctorIdAndBetweenDates(Long doctorId, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd);
}
