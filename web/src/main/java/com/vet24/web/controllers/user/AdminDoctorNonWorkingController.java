package com.vet24.web.controllers.user;

import com.vet24.models.dto.user.CommentDto;
import com.vet24.models.dto.user.DoctorNonWorkingDto;
import com.vet24.models.mappers.user.DoctorNonWorikingMapper;
import com.vet24.models.user.DoctorNonWorking;
import com.vet24.service.user.DoctorNonWorkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/admin/doctor_non_working/")
@Tag(name = "admin doctor_non_working controller", description = "operations with doctor_non_working")
@Slf4j
public class AdminDoctorNonWorkingController {

    private final DoctorNonWorkingService doctorNonWorkingService;
    private final DoctorNonWorikingMapper doctorNonWorikingMapper;

    @Autowired
    public AdminDoctorNonWorkingController(DoctorNonWorkingService doctorNonWorkingService, DoctorNonWorikingMapper doctorNonWorikingMapper) {
        this.doctorNonWorkingService = doctorNonWorkingService;
        this.doctorNonWorikingMapper = doctorNonWorikingMapper;
    }

    @Operation(summary = "create doctorNonWorking")
    @ApiResponse(responseCode = "200", description = "Successfully created new DoctorNonWorking")
    @PostMapping("")
    public ResponseEntity<DoctorNonWorkingDto> createDoctorNonWorking(@Valid @RequestBody DoctorNonWorkingDto doctorNonWorkingDto){
        DoctorNonWorking doctorNonWorking = doctorNonWorikingMapper.toEntity(doctorNonWorkingDto);
        doctorNonWorkingService.persist(doctorNonWorking);
        log.info("DoctorNonWorking create");
        return ResponseEntity.ok(doctorNonWorikingMapper.toDto(doctorNonWorking));
    }



    @Operation(summary = "edit doctorNonWorking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DoctorNonWorking updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorNonWorkingDto.class))),
            @ApiResponse(responseCode = "404", description = "DoctorNonWorking not found")
    })
    @PutMapping("{id}")
    public ResponseEntity<DoctorNonWorkingDto> editDoctorNonWorking(@RequestBody DoctorNonWorkingDto doctorNonWorkingDto,
                                                                   @PathVariable("id") Long id){
        if(doctorNonWorkingService.isExistByKey(id)){
            log.info("DoctorNonWorking with id {}",id);
            DoctorNonWorking doctorNonWorking = doctorNonWorikingMapper.toEntity(doctorNonWorkingDto);
            doctorNonWorking.setId(id);
            doctorNonWorkingService.update(doctorNonWorking);
            return ResponseEntity.ok(doctorNonWorikingMapper.toDto(doctorNonWorking));
        }else {
            log.info("DoctorNonWorking with id {} not found", id);
            throw new NotFoundException("DoctorNonWorking not found");
        }
    }



    @Operation(summary = "delete doctorNonWorking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DoctorNonWorking deleted"),
            @ApiResponse(responseCode = "404", description = "DoctorNonWorking not found")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteDoctorNonWorking(@PathVariable("id") Long id){
        if(doctorNonWorkingService.isExistByKey(id)){
            doctorNonWorkingService.delete(doctorNonWorkingService.getByKey(id));
            log.info("DoctorNonWorking with id {} deleted", id);
        }else {
            log.info("DoctorNonWorking with id {} not found", id);
            throw new NotFoundException("DoctorNonWorking not found");
        }
        return ResponseEntity.ok().build();

    }




}
