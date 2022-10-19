package com.vet24.service.medicine;

import com.vet24.models.medicine.Appointment;
import com.vet24.service.ReadWriteService;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService extends ReadWriteService<Long, Appointment> {

   boolean isExistByDoctorIdAndLocalDateTime(Long doctorId, LocalDateTime dateTime);
}
