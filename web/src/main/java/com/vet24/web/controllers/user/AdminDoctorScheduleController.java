package com.vet24.web.controllers.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.dto.user.DoctorScheduleDto;
import com.vet24.models.mappers.user.DoctorScheduleMapper;
import com.vet24.models.medicine.DoctorSchedule;
import com.vet24.models.util.View;
import com.vet24.service.medicine.DoctorScheduleService;
import com.vet24.service.user.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

@RestController
@RequestMapping(value = "api/admin/schedule")
@Tag(name = "admin doctor schedule controller", description = "adminDoctorScheduleController operations")
@Slf4j
public class AdminDoctorScheduleController {
    private final DoctorScheduleService doctorScheduleService;
    private final DoctorScheduleMapper doctorScheduleMapper;
    private final DoctorService doctorService;

    @Autowired
    public AdminDoctorScheduleController(DoctorScheduleService doctorScheduleService,
                                         DoctorScheduleMapper doctorScheduleMapper,
                                         DoctorService doctorService) {
        this.doctorScheduleService = doctorScheduleService;
        this.doctorScheduleMapper = doctorScheduleMapper;
        this.doctorService = doctorService;
    }

    @Operation(summary = "Create schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule is create",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorScheduleDto.class))),
            @ApiResponse(responseCode = "400", description = "Doctor's id, work shift or week number content can't null"),
            @ApiResponse(responseCode = "404", description = "Doctor not found"),
            @ApiResponse(responseCode = "422", description = "Doctor already has a work shift")
    })
    @PostMapping
    public ResponseEntity<DoctorScheduleDto> createDoctorSchedule(@RequestBody(required = false)
                                                                  @JsonView(View.Post.class)
                                                                  @Validated(OnCreate.class)
                                                                          DoctorScheduleDto doctorScheduleDto) {
        if (!doctorService.isExistByKey(doctorScheduleDto.getDoctorId())) {
            log.error("User with id {} is not a doctor", doctorScheduleDto.getDoctorId());
            throw new NotFoundException("doctor not found");
        } else if (doctorScheduleService
                .isExistByDoctorIdAndWeekNumber(doctorScheduleDto.getDoctorId(), doctorScheduleDto.getWeekNumber())) {
            log.error("Doctor already has a work shift at week {}", doctorScheduleDto.getWeekNumber());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Doctor already has a work shift");
        } else {
            DoctorSchedule doctorSchedule = doctorScheduleMapper.toEntity(doctorScheduleDto);
            doctorSchedule.setDoctor(doctorService.getByKey(doctorScheduleDto.getDoctorId()));
            doctorScheduleService.persist(doctorSchedule);
            log.info("Schedule with id {} created", doctorSchedule.getId());
            return ResponseEntity.ok(doctorScheduleMapper.toDto(doctorSchedule));
        }
    }

    @Operation(summary = "Update schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule is update",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorScheduleDto.class))),
            @ApiResponse(responseCode = "400", description = "Work shift or schedule's id content can't null"),
            @ApiResponse(responseCode = "404", description = "Schedule not found")
    })
    @PutMapping
    public ResponseEntity<DoctorScheduleDto> updateDoctorSchedule(@RequestBody(required = false)
                                                                  @JsonView(View.Put.class)
                                                                  @Validated(OnUpdate.class)
                                                                          DoctorScheduleDto doctorScheduleDto) {
        DoctorSchedule doctorSchedule = doctorScheduleService.getByKey(doctorScheduleDto.getId());
        if (doctorSchedule != null) {
            doctorSchedule.setWorkShift(doctorScheduleDto.getWorkShift());
            doctorScheduleService.update(doctorSchedule);
            log.info("Schedule with id {} updated", doctorScheduleDto.getId());
            return ResponseEntity.ok(doctorScheduleMapper.toDto(doctorSchedule));
        } else {
            log.error("Schedule with id {} not found", doctorScheduleDto.getId());
            throw new NotFoundException("Schedule not found");
        }
    }

    @Operation(summary = "Delete schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule deleted"),
            @ApiResponse(responseCode = "404", description = "Schedule not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctorSchedule(@PathVariable("id") Long id) {
        DoctorSchedule doctorSchedule = doctorScheduleService.getByKey(id);
        if (doctorSchedule != null) {
            doctorScheduleService.delete(doctorSchedule);
            log.info("Schedule with id {} deleted", id);
        } else {
            log.error("Schedule with id {} not found", id);
            throw new NotFoundException("Schedule not found");
        }
        return ResponseEntity.ok().build();
    }
}
