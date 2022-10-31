package com.vet24.web.controllers.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.dto.user.DoctorScheduleDto;
import com.vet24.models.exception.UnprocessableEntityException;
import com.vet24.models.mappers.user.DoctorScheduleMapper;
import com.vet24.models.medicine.DoctorSchedule;
import com.vet24.models.util.View;
import com.vet24.service.medicine.DoctorScheduleService;
import com.vet24.service.user.UserService;
import com.vet24.web.controllers.annotations.CheckExist;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

@RestController
@RequestMapping(value = "api/admin/schedule")
@Tag(name = "admin doctor schedule controller", description = "adminDoctorScheduleController operations")
@Slf4j
public class AdminDoctorScheduleController {
    private final DoctorScheduleService doctorScheduleService;
    private final DoctorScheduleMapper doctorScheduleMapper;
    private final UserService userService;

    @Autowired
    public AdminDoctorScheduleController(DoctorScheduleService doctorScheduleService,
                                         DoctorScheduleMapper doctorScheduleMapper, UserService userService) {
        this.doctorScheduleService = doctorScheduleService;
        this.doctorScheduleMapper = doctorScheduleMapper;
        this.userService = userService;
    }

    @Operation(summary = "Create schedule")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Schedule is create",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorScheduleDto.class))),
            @ApiResponse(responseCode = "404", description = "Doctor not found"),
            @ApiResponse(responseCode = "422", description = "Doctor already has a work shift")})
    @PostMapping
    public ResponseEntity<DoctorScheduleDto> createDoctorSchedule(@RequestBody(required = false) @JsonView(View.Post.class)
                                                                      @Validated(OnCreate.class) DoctorScheduleDto doctorScheduleDto) {
        if (!userService.isExistByKey(doctorScheduleDto.getDoctorId())) {
            log.error("User with id {} is not a doctor", doctorScheduleDto.getDoctorId());
            throw new NotFoundException("doctor not found");
        }
        if (doctorScheduleService.isExistByDoctorIdAndWeekNumber(doctorScheduleDto.getDoctorId(), doctorScheduleDto.getStartWeek())) {
            log.error("Doctor already has a work shift at week {}", doctorScheduleDto.getStartWeek());
            throw new UnprocessableEntityException("Doctor already has a work shift");
        }
        DoctorSchedule doctorSchedule = doctorScheduleMapper.toEntity(doctorScheduleDto);
        doctorSchedule.setDoctor(userService.getByKey(doctorScheduleDto.getDoctorId()));
        doctorScheduleService.persist(doctorSchedule);
        log.info("Schedule with id {} created", doctorSchedule.getId());
        return ResponseEntity.status(201).body(doctorScheduleMapper.toDto(doctorSchedule));
    }

    @Operation(summary = "Update schedule")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Schedule is update",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorScheduleDto.class))),
            @ApiResponse(responseCode = "400", description = "Schedule not found")})

    @PutMapping("/{id}")
    public ResponseEntity<DoctorScheduleDto> updateDoctorSchedule
            (@RequestBody(required = false) @JsonView(View.Put.class)
             @Validated(OnUpdate.class) DoctorScheduleDto doctorScheduleDto,
             @CheckExist (entityClass = DoctorSchedule.class) @PathVariable("id") Long id) {
        DoctorSchedule doctorSchedule = doctorScheduleService.getByKey(id);
            doctorSchedule.setWorkShift(doctorScheduleDto.getWorkShift());
            doctorScheduleService.update(doctorSchedule);
            log.info("Schedule with id {} updated", id);
            return ResponseEntity.ok(doctorScheduleMapper.toDto(doctorSchedule));
    }

    @Operation(summary = "Delete schedule")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Schedule deleted"),
            @ApiResponse(responseCode = "400", description = "Schedule not found")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctorSchedule(@ CheckExist(entityClass = DoctorSchedule.class) @PathVariable("id") Long id) {
        DoctorSchedule doctorSchedule = doctorScheduleService.getByKey(id);
            doctorScheduleService.delete(doctorSchedule);
            log.info("Schedule with id {} deleted", id);
        return ResponseEntity.ok().build();
    }
}
