package com.vet24.web.controllers.user;

import com.vet24.models.dto.appointment.AppointmentCalendarElementDto;
import com.vet24.models.dto.appointment.AppointmentCallendarDto;
import com.vet24.service.medicine.DoctorScheduleService;
import com.vet24.service.medicine.dto.AppointmentCalendarDtoService;
import io.swagger.v3.oas.annotations.media.Content;;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "api/client/appointment")
@Slf4j
@Tag(name = "ClientAppointmentCalendar controller", description = "Получение календаря доступных записей")
public class ClientAppointmentCalendarController {

    private final DoctorScheduleService doctorScheduleService;

    private final AppointmentCalendarDtoService appointmentCalendarElementDtoService;

    public ClientAppointmentCalendarController(DoctorScheduleService doctorScheduleService, AppointmentCalendarDtoService appointmentCalendarElementDtoService) {
        this.doctorScheduleService = doctorScheduleService;
        this.appointmentCalendarElementDtoService = appointmentCalendarElementDtoService;
    }

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Расписание получено",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Расписание можно получать только на текущую и следующую неделю"),
            @ApiResponse(responseCode = "404", description = "Расписания на заданную дату не найдено")
    })
    public ResponseEntity<AppointmentCallendarDto> getAppointmentCalendar(@RequestParam(name = "doctorId", required = false) Long doctorId,
                                                                          @RequestParam(name = "date", required = false, defaultValue = "#{T(java.time.LocalDateTime).now().getDayOfMonth()}") Integer date) {

        LocalDate now = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth());
        LocalDate dateRequest = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), date);
        LocalDate dateDoctor = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), date - (dateRequest.getDayOfWeek().getValue() - 1));

        if (dateRequest.isBefore(now) || dateRequest.isAfter(now.with(DayOfWeek.MONDAY).plusWeeks(2L).minusDays(1L))) {
            log.info("Расписание можно получать только на текущую и следующую неделю");
            return ResponseEntity.badRequest().build();
        }
        if (doctorId != null) {
            if (!doctorScheduleService.isExistByDoctorIdAndWeekNumber(doctorId, dateDoctor)) {
                log.info("Appointment not found");
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(new AppointmentCallendarDto(appointmentCalendarElementDtoService.createAppointmentCalendarDto(doctorId, dateDoctor, dateRequest)));
        } else {
            List<AppointmentCalendarElementDto> list = new ArrayList<>();
            doctorScheduleService.getDoctorScheduleCurrentDate(dateDoctor)
                    .stream()
                    .map(x -> doctorScheduleService.getDoctorId(x))
                    .forEach(x -> {
                        for (int i = 0; i < appointmentCalendarElementDtoService.createAppointmentCalendarDto(x, dateDoctor, dateRequest).size(); i++) {
                            list.add(appointmentCalendarElementDtoService.createAppointmentCalendarDto(x, dateDoctor, dateRequest).get(i));
                        }
                    });
            if (list.size() == 0) {
                log.info("Appointment not found");
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(new AppointmentCallendarDto(list));
        }
    }
}
