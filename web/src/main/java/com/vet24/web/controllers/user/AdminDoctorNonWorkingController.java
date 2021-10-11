package com.vet24.web.controllers.user;

import com.vet24.models.dto.user.DoctorNonWorkingDto;
import com.vet24.models.mappers.user.DoctorNonWorikingMapper;
import com.vet24.models.user.DoctorNonWorking;
import com.vet24.service.user.DoctorNonWorkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/admin/doctor_non_working/")
@Tag(name = "admin doctor_non_working controller", description = "operations with doctor_non_working")
public class AdminDoctorNonWorkingController {

    private final DoctorNonWorkingService doctorNonWorkingService;
    private final DoctorNonWorikingMapper doctorNonWorikingMapper;

    @Autowired
    public AdminDoctorNonWorkingController(DoctorNonWorkingService doctorNonWorkingService, DoctorNonWorikingMapper doctorNonWorikingMapper) {
        this.doctorNonWorkingService = doctorNonWorkingService;
        this.doctorNonWorikingMapper = doctorNonWorikingMapper;
    }

    @Operation(summary = "create doctorNonWorking")

    @PostMapping("")
    public ResponseEntity<DoctorNonWorkingDto> createDoctorNonWorking(@RequestBody DoctorNonWorkingDto doctorNonWorkingDto){
        DoctorNonWorking doctorNonWorking = doctorNonWorikingMapper.toEntity(doctorNonWorkingDto);
        doctorNonWorkingService.persist(doctorNonWorking);
        return ResponseEntity.ok(doctorNonWorikingMapper.toDto(doctorNonWorking));
    }




}
