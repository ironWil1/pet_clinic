package com.vet24.service.medicine.dto;

import com.vet24.models.dto.appointment.AppointmentCalendarElementDto;
import com.vet24.models.dto.appointment.AppointmentDayElementDto;
import com.vet24.models.medicine.DoctorSchedule;
import com.vet24.service.medicine.AppointmentService;
import com.vet24.service.medicine.DoctorScheduleService;
import com.vet24.service.user.DoctorNonWorkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class AppointmentCalendarDtoServiceImpl implements AppointmentCalendarDtoService {

    private final DoctorNonWorkingService doctorNonWorkingService;
    private final DoctorScheduleService doctorScheduleService;

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentCalendarDtoServiceImpl(DoctorNonWorkingService doctorNonWorkingService, DoctorScheduleService doctorScheduleService, AppointmentService appointmentService) {
        this.doctorNonWorkingService = doctorNonWorkingService;
        this.doctorScheduleService = doctorScheduleService;
        this.appointmentService = appointmentService;
    }

    public void createAppointmentDayElementDtoList(LocalDate dateDoctor, int workShiftCount, List<AppointmentCalendarElementDto> calendarElementList,
                                                   List<LocalDate> doctorNonWorkingList, List<LocalDateTime> appointments) {
        for (int i = 0; i < 7; i++) {
            LocalDate localDate = dateDoctor.plusDays(i);
            List<AppointmentDayElementDto> appointmentDayElementDtoList = new ArrayList<>();
            if (doctorNonWorkingList.contains(localDate)) {
                IntStream.range(0, 16).forEach(j -> appointmentDayElementDtoList.add(new AppointmentDayElementDto(LocalTime.of(7 + j, 0), false)));
                calendarElementList.add(new AppointmentCalendarElementDto(localDate, appointmentDayElementDtoList));
                continue;
            }
            if (workShiftCount == 7) {
                intStreamRange(appointments, localDate, workShiftCount, appointmentDayElementDtoList);
                IntStream.range(15, 23).forEach(j -> appointmentDayElementDtoList.add(new AppointmentDayElementDto(LocalTime.of(j, 0), false)));
            } else if (workShiftCount == 15) {
                IntStream.range(7, 15).forEach(j -> appointmentDayElementDtoList.add(new AppointmentDayElementDto(LocalTime.of(j, 0), false)));
                intStreamRange(appointments, localDate, workShiftCount, appointmentDayElementDtoList);
            } else if (workShiftCount == 1) {
                IntStream.range(0, 16).forEach(j -> {
                    if (appointments.contains(LocalDateTime.of(localDate, LocalTime.of(7 + j, 0)))) {
                        appointmentDayElementDtoList.add(new AppointmentDayElementDto(LocalTime.of(7 + j, 0), false));
                    } else {
                        appointmentDayElementDtoList.add(new AppointmentDayElementDto(LocalTime.of(7 + j, 0), (localDate.isAfter(LocalDate.now()) || localDate.isEqual(LocalDate.now()))));
                    }
                });
            }
            calendarElementList.add(new AppointmentCalendarElementDto(localDate, appointmentDayElementDtoList));
        }
    }

    @Override
    public List<AppointmentCalendarElementDto> createAppointmentCalendarDto(Long doctorId, LocalDate dateDoctor) {
        List<AppointmentCalendarElementDto> appointmentCalendarElementDto = new ArrayList<>();
        String workShift = doctorScheduleService.getDoctorScheduleWorkShift(doctorId, dateDoctor);
        List<LocalDate> doctorNonWorkingList = doctorNonWorkingService.getDateByDoctorIdAndDate(doctorId, dateDoctor, dateDoctor.with(DayOfWeek.SUNDAY));
        List<LocalDateTime> appointments = new ArrayList<>();
        int workShiftCount;

        if (workShift.equals("FIRST_SHIFT")) {
            workShiftCount = 7;
            createAppointmentDayElementDtoList(dateDoctor, workShiftCount, appointmentCalendarElementDto, doctorNonWorkingList, appointments);
        } else if (workShift.equals("SECOND_SHIFT")) {
            workShiftCount = 15;
            createAppointmentDayElementDtoList(dateDoctor,workShiftCount, appointmentCalendarElementDto, doctorNonWorkingList, appointments);
        }
        return appointmentCalendarElementDto;
    }
    public void intStreamRange(List<LocalDateTime> appointments, LocalDate localDate, int workShiftCount, List<AppointmentDayElementDto> appointmentDayElementDtoList) {
        IntStream.range(0, 8).forEach(j -> {
            if (appointments.contains(LocalDateTime.of(localDate, LocalTime.of(workShiftCount + j, 0)))) {
                appointmentDayElementDtoList.add(new AppointmentDayElementDto(LocalTime.of(workShiftCount + j, 0), false));
            } else {
                appointmentDayElementDtoList.add(new AppointmentDayElementDto(LocalTime.of(workShiftCount + j, 0), (localDate.isAfter(LocalDate.now()) || localDate.isEqual(LocalDate.now()))));
            }
        });
    }

    public <T> List<T> commonElements(Iterable<? extends List<? extends T>> lists) {
        Iterator<? extends List<? extends T>> it = lists.iterator();
        List<T> commonElements = new ArrayList<T>(it.next());
        while (it.hasNext()) {
            commonElements.retainAll(it.next());
        }
        return commonElements;
    }

    @Override
    public List<AppointmentCalendarElementDto> createAppointmentCalendarDtoWithoutDoctorId(LocalDate dateDoctor, List<DoctorSchedule> doctorScheduleList) {
        List<AppointmentCalendarElementDto> appointmentCalendarElementDto = new ArrayList<>();
        Map<Long, String> workShiftMap = new HashMap<>();
        List<Long> doctorsIdList = new ArrayList<>();
        List<List<LocalDate>> doctorNonWorkingLists = new ArrayList<>();
        List<List<LocalDateTime>> appointments = new ArrayList<>();
        doctorScheduleList.stream()
                .forEach(x -> doctorsIdList.add(doctorScheduleService.getDoctorId(x)));
        doctorsIdList.stream()
                .forEach(x -> workShiftMap.put(x, doctorScheduleService.getDoctorScheduleWorkShift(x, dateDoctor)));
        doctorsIdList.stream()
                .forEach(x -> doctorNonWorkingLists.add(doctorNonWorkingService.getDateByDoctorIdAndDate(x, dateDoctor, dateDoctor.with(DayOfWeek.SUNDAY))));
        workShiftMap.entrySet().stream()
                .forEach(x -> appointments.add(appointmentService.getLocalDateTimeByDoctorIdAndDate(x.getKey(), LocalDateTime.of(dateDoctor, LocalTime.of(x.getValue().equals("FIRST_SHIFT") ? 7 : 15, 0)),
                        LocalDateTime.of(dateDoctor, LocalTime.of(x.getValue().equals("FIRST_SHIFT") ? 7 : 15, 0)).plusDays(6L))));

        List<LocalDate> commonDoctorNonWorking = commonElements(doctorNonWorkingLists);
        List<LocalDateTime> commonAppointments = commonElements(appointments);

        int workShiftCount;

        if (!workShiftMap.containsValue("FIRST_SHIFT")) {
            workShiftCount = 15;
        } else if (!workShiftMap.containsValue("SECOND_SHIFT")) {
            workShiftCount = 7;
        } else {
            workShiftCount = 1;
        }
        createAppointmentDayElementDtoList(dateDoctor, workShiftCount, appointmentCalendarElementDto, commonDoctorNonWorking, commonAppointments);
        return appointmentCalendarElementDto;
    }

    @Override
    public List<AppointmentCalendarElementDto> doctorScheduleNotFound(LocalDate dateDoctor) {
        List<AppointmentCalendarElementDto> appointmentCalendarElementDto = new ArrayList<>();
        List<AppointmentDayElementDto> appointmentDayElementDtoList = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate localDate = dateDoctor.plusDays(i);
            IntStream.range(0, 16).forEach(j -> appointmentDayElementDtoList.add(new AppointmentDayElementDto(LocalTime.of(7 + j, 0), false)));
            appointmentCalendarElementDto.add(new AppointmentCalendarElementDto(localDate, appointmentDayElementDtoList));
        }
        return  appointmentCalendarElementDto;
    }
}
