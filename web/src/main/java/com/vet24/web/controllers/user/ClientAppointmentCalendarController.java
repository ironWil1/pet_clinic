package com.vet24.web.controllers.user;

import com.vet24.models.dto.appointment.AppointmentCallendarDto;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.medicine.DoctorSchedule;
import com.vet24.models.user.Role;
import com.vet24.service.medicine.DoctorScheduleService;
import com.vet24.service.medicine.dto.AppointmentCalendarDtoService;
import com.vet24.service.user.UserService;
import io.swagger.v3.oas.annotations.media.Content;;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(value = "api/client/appointment")
@Slf4j
@Tag(name = "ClientAppointmentCalendar controller", description = "Получение календаря доступных записей")
public class ClientAppointmentCalendarController {

    private final DoctorScheduleService doctorScheduleService;

    private final AppointmentCalendarDtoService appointmentCalendarElementDtoService;

    private UserService userService;

    public ClientAppointmentCalendarController(DoctorScheduleService doctorScheduleService, AppointmentCalendarDtoService appointmentCalendarElementDtoService, UserService userService) {
        this.doctorScheduleService = doctorScheduleService;
        this.appointmentCalendarElementDtoService = appointmentCalendarElementDtoService;
        this.userService = userService;
    }

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Расписание получено",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Расписание можно получать только на текущую и следующую неделю/доктора с заданным Id не существует")
    })
    public ResponseEntity<AppointmentCallendarDto> getAppointmentCalendar(@RequestParam(name = "doctorId", required = false) Long doctorId,
                                                                          @RequestParam(name = "date", required = false, defaultValue = "#{T(java.time.LocalDateTime).now()}") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        if (date.isBefore(LocalDate.now()) || date.isAfter(LocalDate.now().with(DayOfWeek.MONDAY).plusWeeks(2L).minusDays(1L))) {
            log.info("Расписание можно получать только на текущую и следующую неделю");
            throw new BadRequestException("Расписание можно получать только на текущую и следующую неделю");
        }
        if (doctorId != null) {
            if (!userService.isExistByIdAndRole(doctorId, RoleNameEnum.DOCTOR)) {
                log.info("Доктора с заданным Id не существует");
                throw new BadRequestException("Доктора с заданным Id не существует");
            }
            return ResponseEntity.ok(appointmentCalendarElementDtoService.createAppointmentCalendarDto(doctorId, date.with(DayOfWeek.MONDAY)));
        } else {
            return ResponseEntity.ok(appointmentCalendarElementDtoService.createAppointmentCalendarDtoWithoutDoctorId(date.with(DayOfWeek.MONDAY)));
        }
    }
}
