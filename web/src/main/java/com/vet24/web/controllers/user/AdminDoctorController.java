package com.vet24.web.controllers.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.user.DoctorDto;
import com.vet24.models.dto.user.DoctorDtoPost;
import com.vet24.models.mappers.user.DoctorPostMapper;
import com.vet24.models.mappers.user.DoctorMapper;
import com.vet24.models.user.Doctor;
import com.vet24.models.util.View;
import com.vet24.service.user.DoctorServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/admin/user/doctor")
@Tag(name = "AdminDoctor controller", description = "CRUD operations")
public class AdminDoctorController {

    private final DoctorServiceImpl doctorServiceImpl;
    private final DoctorMapper doctorMapper;
    private final DoctorPostMapper doctorPostMapper;


    public AdminDoctorController(DoctorServiceImpl doctorServiceImpl, DoctorMapper doctorMapper, DoctorPostMapper doctorPostMapper) {
        this.doctorServiceImpl = doctorServiceImpl;
        this.doctorMapper = doctorMapper;
        this.doctorPostMapper = doctorPostMapper;
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


    @Operation(summary = "Create new doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor is create",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDtoPost.class))),
            @ApiResponse(responseCode = "404", description = "Doctor not found"),
    })
    @PostMapping
    public ResponseEntity<DoctorDto> doctorDtoPost(@JsonView(View.Post.class) @Valid @RequestBody DoctorDtoPost doctorDtoPost) {
        Doctor doctor = doctorPostMapper.toEntity(doctorDtoPost);
        doctorServiceImpl.persist(doctor);
        return ResponseEntity.status(200).body(doctorPostMapper.toDto(doctor));
    }

    @Operation(summary = "Update doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor is update",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDtoPost.class))),
            @ApiResponse(responseCode = "404", description = "Doctor not found"),
    })
    @PutMapping("{id}")
    public ResponseEntity<DoctorDto> doctorDtoPut(@JsonView(View.Put.class) @Valid @RequestBody DoctorDtoPost doctorDtoPost,
                                                  @PathVariable("id") long id) {
        Doctor doctor = doctorServiceImpl.getByKey(id);
        doctor.setFirstname(doctorDtoPost.getFirstname());
        doctor.setLastname(doctorDtoPost.getLastname());
        doctor.setPassword(doctorDtoPost.getPassword());
        doctor.setAvatar(doctorDtoPost.getAvatar());
        doctorServiceImpl.update(doctor);
        return ResponseEntity.ok(doctorMapper.toDto(doctor));
    }

    @Operation(summary = "Delete doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor removed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDtoPost.class))),
            @ApiResponse(responseCode = "404", description = "Doctor not found"),
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteDto(@PathVariable("id") long id) {
        Doctor doctor = doctorServiceImpl.getByKey(id);


        if (doctor != null) {

            doctorServiceImpl.delete(doctor);
            return ResponseEntity.ok().build();
        } else {
            log.info("The current doctor is not found");
            throw new NotFoundException("Doctor not found");
        }
    }


}