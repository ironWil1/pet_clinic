package com.vet24.web.controllers.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnCreateDoctorSchedule;
import com.vet24.models.dto.OnUpdateDoctorSchedule;
import com.vet24.models.dto.user.DoctorScheduleDto;
import com.vet24.models.mappers.user.DoctorScheduleMapper;
import com.vet24.models.mappers.user.UserInfoMapper;
import com.vet24.models.medicine.DoctorSchedule;
import com.vet24.models.user.Doctor;
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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

@RestController
@RequestMapping(value = "api/admin/schedule")
@Tag(name = "admin doctor schedule controller", description = "adminDoctorScheduleController operations")
@Slf4j
public class AdminDoctorScheduleController {
    private final DoctorScheduleService doctorScheduleService;
    private final DoctorScheduleMapper doctorScheduleMapper;
    private final DoctorService doctorService;
    private final UserInfoMapper userInfoMapper;

    @Autowired
    public AdminDoctorScheduleController(DoctorScheduleService doctorScheduleService, DoctorScheduleMapper doctorScheduleMapper, DoctorService doctorService, UserInfoMapper userInfoMapper) {
        this.doctorScheduleService = doctorScheduleService;
        this.doctorScheduleMapper = doctorScheduleMapper;
        this.doctorService = doctorService;
        this.userInfoMapper = userInfoMapper;
    }

    @Operation(summary = "create schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "schedule is create",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorScheduleDto.class))),
            @ApiResponse(responseCode = "400", description = "doctorId, workShift or weekNumber content can't null")
    })
    @PostMapping
    public ResponseEntity<DoctorScheduleDto> createDoctorSchedule(@JsonView(View.PostAdminSchedule.class)
                                                                  @RequestBody(required = false)
                                                                  @Validated(OnCreateDoctorSchedule.class)
                                                                          DoctorScheduleDto doctorScheduleDto) {
        Doctor doctor = doctorService.getByKey(doctorScheduleDto.getDoctorInfo().getId());
        if (doctor != null) {
            doctorScheduleDto.setDoctorInfo(userInfoMapper.toDto(doctor));
            DoctorSchedule doctorSchedule = doctorScheduleMapper.toEntity(doctorScheduleDto);
            doctorScheduleService.persist(doctorSchedule);
            return ResponseEntity.ok(doctorScheduleMapper.toDto(doctorSchedule));
        } else {
            log.error("User with id {} is not a doctor", doctorScheduleDto.getDoctorInfo().getId());
            throw new NotFoundException("this is not a doctor");
        }
    }

    @Operation(summary = "update schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "schedule is update",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorScheduleDto.class))),
            @ApiResponse(responseCode = "400", description = "workShift or weekNumber content can't null"),
            @ApiResponse(responseCode = "404", description = "schedule not found")
    })
    @PutMapping
    public ResponseEntity<DoctorScheduleDto> updateDoctorSchedule(@JsonView(View.PutAdminSchedule.class)
                                                                  @RequestBody(required = false)
                                                                  @Validated(OnUpdateDoctorSchedule.class)
                                                                          DoctorScheduleDto doctorScheduleDto) {
        DoctorSchedule doctorSchedule = doctorScheduleService.getByKey(doctorScheduleDto.getId());
        if (doctorSchedule != null) {
            doctorSchedule.setWorkShift(doctorScheduleDto.getWorkShift());
            doctorSchedule.setWeekNumber(doctorScheduleDto.getWeekNumber());
            doctorScheduleService.update(doctorSchedule);
            log.info("Schedule with id {} updated", doctorScheduleDto.getId());
            return ResponseEntity.ok(doctorScheduleMapper.toDto(doctorSchedule));
        } else {
            log.error("Schedule with id {} not found", doctorScheduleDto.getId());
            throw new NotFoundException("Schedule not found");
        }
    }

    @Operation(summary = "delete schedule")
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
