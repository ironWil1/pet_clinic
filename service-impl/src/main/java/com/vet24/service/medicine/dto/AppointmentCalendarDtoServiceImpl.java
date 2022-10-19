package com.vet24.service.medicine.dto;

import com.vet24.models.dto.appointment.AppointmentCalendarElementDto;
import com.vet24.models.dto.appointment.AppointmentDayElementDto;
import com.vet24.service.medicine.AppointmentService;
import com.vet24.service.medicine.DoctorScheduleService;
import com.vet24.service.user.DoctorNonWorkingService;
import com.vet24.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class AppointmentCalendarDtoServiceImpl implements AppointmentCalendarDtoService {

    private final DoctorNonWorkingService doctorNonWorkingService;

    private final UserService userService;

    private final DoctorScheduleService doctorScheduleService;

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentCalendarDtoServiceImpl(DoctorNonWorkingService doctorNonWorkingService, UserService userService, DoctorScheduleService doctorScheduleService, AppointmentService appointmentService) {
        this.doctorNonWorkingService = doctorNonWorkingService;
        this.userService = userService;
        this.doctorScheduleService = doctorScheduleService;
        this.appointmentService = appointmentService;
    }

    @Override
    public List<AppointmentCalendarElementDto> createAppointmentCalendarDto(Long doctorId, LocalDate dateDoctor, LocalDate dateRequest) {
        List<AppointmentCalendarElementDto> appointmentCalendarElementDto = new ArrayList<>();

        //Работа со сменой доктора
        for (int i = 0; i < 7; i++) {
            LocalDate localDate = dateDoctor.plusDays(i);
            if (doctorNonWorkingService.isExistByDoctorIdAndDate(userService.getByKey(doctorId), localDate))  {
                continue;
            }
            List<AppointmentDayElementDto> appointmentDayElementDtoList = new ArrayList<>();
            IntStream.range(0, 8).forEach(j -> {
                if (doctorScheduleService.getDoctorScheduleWorkShift(doctorId, dateDoctor).equals("FIRST_SHIFT") && !appointmentService.isExistByDoctorIdAndLocalDateTime(doctorId, LocalDateTime.of(localDate, LocalTime.of(7 + j, 0)))) {
                    appointmentDayElementDtoList.add(new AppointmentDayElementDto(LocalTime.of(7 + j, 0), (dateRequest.getDayOfMonth() <= localDate.getDayOfMonth())));
                } else if (doctorScheduleService.getDoctorScheduleWorkShift(doctorId, dateDoctor).equals("SECOND_SHIFT") && !appointmentService.isExistByDoctorIdAndLocalDateTime(doctorId, LocalDateTime.of(localDate, LocalTime.of(15 + j, 0)))) {
                    appointmentDayElementDtoList.add(new AppointmentDayElementDto(LocalTime.of(15 + j, 0), (dateRequest.getDayOfMonth() <= localDate.getDayOfMonth())));
                }
            });
            appointmentCalendarElementDto.add(new AppointmentCalendarElementDto(localDate, appointmentDayElementDtoList));
        }
        return appointmentCalendarElementDto;
    }
}
