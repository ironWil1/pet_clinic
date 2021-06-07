package com.vet24.web.controllers.medicine;

import com.vet24.models.dto.medicine.DiagnosisDto;
import com.vet24.models.mappers.medicine.DiagnosisMapper;
import com.vet24.models.mappers.user.DoctorMapper;
import com.vet24.models.medicine.Diagnosis;
import com.vet24.models.pet.Pet;
import com.vet24.models.user.Doctor;
import com.vet24.service.medicine.DiagnosisService;
import com.vet24.service.pet.PetService;
import com.vet24.service.user.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

@RestController
@Tag(name = "doctor-controller", description = "Doctor's operations")
public class DoctorController {
    private final PetService petService;
    private final DoctorService doctorService;
    private final DiagnosisService diagnosisService;
    private final DoctorMapper doctorMapper;
    private final DiagnosisMapper diagnosisMapper;
    public static final String ADD_DIAGNOSIS_API ="/api/doctor/pet/{petId}/addDiagnosis";

    public DoctorController(PetService petService, DoctorService doctorService, DiagnosisService diagnosisService, DoctorMapper doctorMapper, DiagnosisMapper diagnosisMapper) {
        this.petService = petService;
        this.doctorService = doctorService;
        this.diagnosisService = diagnosisService;
        this.doctorMapper = doctorMapper;
        this.diagnosisMapper = diagnosisMapper;
    }

    @Operation(summary = "add a new diagnosis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added new diagnosis",
                    content = @Content(schema = @Schema(implementation = DiagnosisDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet is not found")
    })

    @PostMapping(ADD_DIAGNOSIS_API)
    public ResponseEntity<DiagnosisDto> addDiagnosis(@PathVariable Long petId,
                                                     @RequestBody String text){
        Doctor doctor = doctorService.getCurrentDoctor();
        Pet pet = petService.getByKey(petId);
        if(pet == null){
            throw new NotFoundException("No such pet found");
        }
        Diagnosis diagnosis = new Diagnosis(doctor,pet,text);
        diagnosisService.persist(diagnosis);
        return new ResponseEntity<>(diagnosisMapper.diagnosisToDiagnosisDto(diagnosis),
                HttpStatus.CREATED);
    }

}
