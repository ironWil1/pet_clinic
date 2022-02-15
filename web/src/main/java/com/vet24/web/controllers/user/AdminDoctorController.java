package com.vet24.web.controllers.user;

import com.vet24.models.dto.user.DoctorDto;
import com.vet24.models.mappers.user.DoctorMapper;
import com.vet24.models.user.Doctor;
import com.vet24.service.user.DoctorServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/admin/user/doctor")
@Tag(name = "AdminDoctor controller", description = "CRUD operations")
public class AdminDoctorController {

    private final DoctorServiceImpl doctorServiceImpl;
    private final DoctorMapper doctorMapper;

    public AdminDoctorController(DoctorServiceImpl doctorServiceImpl, DoctorMapper doctorMapper) {
        this.doctorServiceImpl = doctorServiceImpl;
        this.doctorMapper = doctorMapper;
    }

    @GetMapping("{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorServiceImpl.getByKey(id);
        if (doctor != null) {
            DoctorDto doctorDto = doctorMapper.toDto(doctor);
            return ResponseEntity.ok(doctorDto);
        } else {
            log.info("The current doctor is not found");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        List<Doctor> doctorList = doctorServiceImpl.getAll();
        return ResponseEntity.ok(doctorMapper.toDto(doctorList));
    }
}