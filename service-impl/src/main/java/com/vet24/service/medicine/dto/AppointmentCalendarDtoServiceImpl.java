package com.vet24.service.medicine.dto;

import com.vet24.models.dto.appointment.AppointmentCalendarElementDto;
import com.vet24.models.dto.appointment.AppointmentCallendarDto;
import com.vet24.models.dto.appointment.AppointmentDayElementDto;
import com.vet24.models.enums.WorkShift;
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

    private List<AppointmentCalendarElementDto> createAppointmentCalendarElementDto(LocalDate firstDayOfWeek, WorkShift workShift,
                                                   List<LocalDate> doctorNonWorkingList, List<LocalDateTime> appointments) {
        List<AppointmentCalendarElementDto> calendarElementList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {

            LocalDate currentDayOfWeek = firstDayOfWeek.plusDays(i);
            List<AppointmentDayElementDto> appointmentDayElementDtoList = new ArrayList<>();
            if (doctorNonWorkingList.contains(currentDayOfWeek)) {
                IntStream.range(7, 23).forEach(j -> appointmentDayElementDtoList.add(new AppointmentDayElementDto(LocalTime.of(j, 0), false)));
                calendarElementList.add(new AppointmentCalendarElementDto(currentDayOfWeek, appointmentDayElementDtoList));
                continue;
            }

            if (workShift.equals(WorkShift.FIRST_SHIFT)) {
                addAvailableAppointmentDayElementDto(appointments, currentDayOfWeek, 7, appointmentDayElementDtoList);
                addHalfDayUnavailableAppointmentDayElementDto(appointmentDayElementDtoList, workShift);
            } else if (workShift.equals(WorkShift.SECOND_SHIFT)) {
                addHalfDayUnavailableAppointmentDayElementDto(appointmentDayElementDtoList, workShift);
                addAvailableAppointmentDayElementDto(appointments, currentDayOfWeek, 15, appointmentDayElementDtoList);
            }
            calendarElementList.add(new AppointmentCalendarElementDto(currentDayOfWeek, appointmentDayElementDtoList));
        }
        return calendarElementList;
    }

    @Override
    public AppointmentCallendarDto createAppointmentCalendarDto(Long doctorId, LocalDate firstDayOfWeek) {

        if (!doctorScheduleService.isExistByDoctorIdAndWeekNumber(doctorId, firstDayOfWeek)) {
            return new AppointmentCallendarDto(createFullDayUnavailableAppointmentDayElementDto(firstDayOfWeek));
        }

        WorkShift workShift = doctorScheduleService.getDoctorScheduleWorkShift(doctorId, firstDayOfWeek);
        List<LocalDate> doctorNonWorkingList = doctorNonWorkingService.getNonWorkingDatesByDoctorIdAndBetweenDates(doctorId, firstDayOfWeek, firstDayOfWeek.with(DayOfWeek.SUNDAY));
        List<LocalDateTime> appointments = appointmentService.getLocalDateTimeByDoctorIdAndBetweenDates(doctorId, LocalDateTime.of(firstDayOfWeek, LocalTime.of(7, 0)), LocalDateTime.of(firstDayOfWeek.with(DayOfWeek.SUNDAY), LocalTime.of(23, 0)));

        if (!doctorScheduleService.isExistByDoctorIdAndWeekNumber(doctorId, firstDayOfWeek)) {
            return new AppointmentCallendarDto(createFullDayUnavailableAppointmentDayElementDto(firstDayOfWeek));
        }
        return new AppointmentCallendarDto(createAppointmentCalendarElementDto(firstDayOfWeek, workShift, doctorNonWorkingList, appointments));
    }

    private void addAvailableAppointmentDayElementDto(List<LocalDateTime> appointments, LocalDate currentDayOfWeek, int workShiftCount, List<AppointmentDayElementDto> appointmentDayElementDtoList) {
        IntStream.range(0, 8).forEach(j -> {
            if (appointments.contains(LocalDateTime.of(currentDayOfWeek, LocalTime.of(workShiftCount + j, 0)))) {
                appointmentDayElementDtoList.add(new AppointmentDayElementDto(LocalTime.of(workShiftCount + j, 0), false));
            } else {
                appointmentDayElementDtoList.add(new AppointmentDayElementDto(LocalTime.of(workShiftCount + j, 0), (!currentDayOfWeek.isBefore(LocalDate.now()))));
            }
        });
    }

    private void addHalfDayUnavailableAppointmentDayElementDto (List<AppointmentDayElementDto> appointmentDayElementDtoList, WorkShift workShift) {
        int startOfRange = workShift.equals(WorkShift.FIRST_SHIFT) ? 15 : 7;
        int endOfRange = workShift.equals(WorkShift.FIRST_SHIFT) ? 23 : 15;

        IntStream.range(startOfRange, endOfRange).forEach(j -> appointmentDayElementDtoList.add(new AppointmentDayElementDto(LocalTime.of(j, 0), false)));
    }

    private  <T> List<T> commonElements(Iterable<? extends List<? extends T>> lists) throws NullPointerException {
        Iterator<? extends List<? extends T>> it = lists.iterator();
        List<T> commonElements = new ArrayList<T>(it.next());

        while (it.hasNext()) {
            commonElements.retainAll(it.next());
        }
        return commonElements;
    }

    @Override
    public AppointmentCallendarDto createAppointmentCalendarDtoWithoutDoctorId(LocalDate firstDayOfWeek) {

        Map<Long, WorkShift> workShiftMap = new HashMap<>();
        Map<Long, List<LocalDate>> doctorNonWorkingMap = new HashMap<>();
        Map<Long, List<LocalDateTime>> appointmentsMap = new HashMap<>();
        List<DoctorSchedule> doctorScheduleList = doctorScheduleService.getDoctorScheduleCurrentDate(firstDayOfWeek);

        if (doctorScheduleList.size() == 0) {
            return new AppointmentCallendarDto(createFullDayUnavailableAppointmentDayElementDto(firstDayOfWeek));
        }
        doctorScheduleList.stream()
                .forEach(x -> workShiftMap.put(x.getDoctor().getId(), x.getWorkShift()));
        workShiftMap.keySet().stream()
                .forEach(x -> doctorNonWorkingMap.put(x, doctorNonWorkingService.getNonWorkingDatesByDoctorIdAndBetweenDates(x, firstDayOfWeek, firstDayOfWeek.with(DayOfWeek.SUNDAY))));
        workShiftMap.entrySet().stream()
                .forEach(x -> appointmentsMap.put(x.getKey(), appointmentService.getLocalDateTimeByDoctorIdAndBetweenDates(x.getKey(),
                        LocalDateTime.of(firstDayOfWeek, LocalTime.of(x.getValue().equals(WorkShift.FIRST_SHIFT) ? 7 : 15, 0)),
                        LocalDateTime.of(firstDayOfWeek, LocalTime.of(x.getValue().equals(WorkShift.FIRST_SHIFT) ? 15 : 22, 0)).plusDays(6L))));

        List<LocalDate> commonDoctorNonWorking = commonElements(doctorNonWorkingMap.values());
        List<LocalDateTime> commonAppointments = commonElements(appointmentsMap.values());

        List<WorkShift> currentWorkShift = new ArrayList<>();

        if (!workShiftMap.containsValue(WorkShift.FIRST_SHIFT)) {
            currentWorkShift.add(WorkShift.SECOND_SHIFT);
        } else if (!workShiftMap.containsValue(WorkShift.SECOND_SHIFT)) {
            currentWorkShift.add(WorkShift.FIRST_SHIFT);
        } else {

            currentWorkShift.addAll(Arrays.asList(WorkShift.FIRST_SHIFT, WorkShift.SECOND_SHIFT));

            Map<Long, List<LocalDate>> doctorNonWorkingForFirstWorkShift = new HashMap<>();
            workShiftMap.entrySet().stream()
                    .filter(x -> x.getValue().equals(WorkShift.FIRST_SHIFT))
                    .forEach(x -> doctorNonWorkingForFirstWorkShift.put(x.getKey(), doctorNonWorkingMap.get(x.getKey())));

            Map<Long, List<LocalDate>> doctorNonWorkingForSecondWorkShift = new HashMap<>();
            workShiftMap.entrySet().stream()
                    .filter(x -> x.getValue().equals(WorkShift.SECOND_SHIFT))
                    .forEach(x -> doctorNonWorkingForSecondWorkShift.put(x.getKey(), doctorNonWorkingMap.get(x.getKey())));

            List<LocalDate> commonDoctorNonWorkingForFirstWorkShift = commonElements(doctorNonWorkingForFirstWorkShift.values());//NullPointer
            List<LocalDate> commonDoctorNonWorkingForSecondWorkShift = commonElements(doctorNonWorkingForSecondWorkShift.values());

            if (doctorNonWorkingForFirstWorkShift.size() == 1) {
                doctorNonWorkingForFirstWorkShift.keySet().forEach(x -> commonDoctorNonWorkingForFirstWorkShift.addAll(doctorNonWorkingMap.get(x)));
                doctorNonWorkingForFirstWorkShift.keySet().forEach(x -> commonAppointments.addAll(appointmentsMap.get(x)));
            }
            if (doctorNonWorkingForSecondWorkShift.size() == 1) {
                doctorNonWorkingForSecondWorkShift.keySet().forEach(x -> commonDoctorNonWorkingForSecondWorkShift.addAll(doctorNonWorkingMap.get(x)));
                doctorNonWorkingForSecondWorkShift.keySet().forEach(x -> commonAppointments.addAll(appointmentsMap.get(x)));
            }
            return new AppointmentCallendarDto(createAppointmentForDifferentWorkShifts(commonDoctorNonWorkingForFirstWorkShift, commonDoctorNonWorkingForSecondWorkShift, commonAppointments, firstDayOfWeek, currentWorkShift));
        }
        return new AppointmentCallendarDto(createAppointmentCalendarElementDto(firstDayOfWeek, currentWorkShift.get(0), commonDoctorNonWorking, commonAppointments));
    }

    private List<AppointmentCalendarElementDto> createAppointmentForDifferentWorkShifts(List<LocalDate> commonDoctorNonWorkingForFirstWorkShift, List<LocalDate> commonDoctorNonWorkingForSecondWorkShift,
                                                                                        List<LocalDateTime> commonAppointments, LocalDate firstDayOfWeek, List<WorkShift> currentWorkShift) {
        List<AppointmentCalendarElementDto> calendarElementList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate currentDayOfWeek = firstDayOfWeek.plusDays(i);
            List<AppointmentDayElementDto> appointmentDayElementDtoList = new ArrayList<>();
            if (commonDoctorNonWorkingForFirstWorkShift.contains(currentDayOfWeek)) {
                addHalfDayUnavailableAppointmentDayElementDto(appointmentDayElementDtoList, currentWorkShift.get(1));
            } else {
                addAvailableAppointmentDayElementDto(commonAppointments, currentDayOfWeek, 7, appointmentDayElementDtoList);
            }
            if (commonDoctorNonWorkingForSecondWorkShift.contains(currentDayOfWeek)) {
                addHalfDayUnavailableAppointmentDayElementDto(appointmentDayElementDtoList, currentWorkShift.get(0));
            } else {
                addAvailableAppointmentDayElementDto(commonAppointments, currentDayOfWeek, 15, appointmentDayElementDtoList);
            }
            calendarElementList.add(new AppointmentCalendarElementDto(currentDayOfWeek, appointmentDayElementDtoList));
        }
        return calendarElementList;
    }

    private List<AppointmentCalendarElementDto> createFullDayUnavailableAppointmentDayElementDto (LocalDate firstDayOfWeek) {
        List<AppointmentCalendarElementDto> appointmentCalendarElementDto = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate currentDayOfWeek = firstDayOfWeek.plusDays(i);
            List<AppointmentDayElementDto> appointmentDayElementDtoList = new ArrayList<>();
            IntStream.range(7, 23).forEach(j -> appointmentDayElementDtoList.add(new AppointmentDayElementDto(LocalTime.of(j, 0), false)));
            appointmentCalendarElementDto.add(new AppointmentCalendarElementDto(currentDayOfWeek, appointmentDayElementDtoList));
        }
        return  appointmentCalendarElementDto;
    }
}
