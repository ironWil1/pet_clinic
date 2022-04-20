package com.vet24.web.controllers.medicine;

import com.vet24.models.dto.medicine.DiagnosisDto;
import com.vet24.models.dto.medicine.TreatmentDto;
import com.vet24.models.dto.pet.procedure.AbstractNewProcedureDto;
import com.vet24.models.mappers.medicine.DiagnosisMapper;
import com.vet24.models.mappers.medicine.TreatmentMapper;
import com.vet24.models.mappers.pet.procedure.AbstractNewProcedureMapper;
import com.vet24.models.medicine.Diagnosis;
import com.vet24.models.medicine.Treatment;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.procedure.Procedure;
import com.vet24.models.user.Doctor;
import com.vet24.service.medicine.DiagnosisService;
import com.vet24.service.medicine.TreatmentService;
import com.vet24.service.pet.PetService;
import com.vet24.service.pet.procedure.ProcedureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import java.util.List;

//import static com.vet24.models.secutity.SecurityUtil.getPrincipalOrNull;

@RestController
@Slf4j
@RequestMapping("/api/doctor")
@Tag(name = "doctor-controller", description = "Doctor's operations")
public class DoctorController {
    private final PetService petService;
    private final DiagnosisService diagnosisService;
    private final DiagnosisMapper diagnosisMapper;
    private final TreatmentService treatmentService;
    private final TreatmentMapper treatmentMapper;
    private final ProcedureService procedureService;
    private final AbstractNewProcedureMapper abstractNewProcedureMapper;


    public DoctorController(PetService petService, DiagnosisService diagnosisService,
                            DiagnosisMapper diagnosisMapper, TreatmentService treatmentService,
                            TreatmentMapper treatmentMapper, ProcedureService procedureService,
                            AbstractNewProcedureMapper abstractNewProcedureMapper) {
        this.petService = petService;
        this.diagnosisService = diagnosisService;
        this.diagnosisMapper = diagnosisMapper;
        this.treatmentService = treatmentService;
        this.treatmentMapper = treatmentMapper;
        this.procedureService = procedureService;
        this.abstractNewProcedureMapper = abstractNewProcedureMapper;
    }

    @Operation(summary = "add a new diagnosis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added new diagnosis",
                    content = @Content(schema = @Schema(implementation = DiagnosisDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet is not found")
    })

    @PostMapping("/pet/{petId}/addDiagnosis")
    public ResponseEntity<DiagnosisDto> addDiagnosis(@PathVariable Long petId,
                                                     @RequestBody String text){
        Pet pet = petService.getByKey(petId);
        if(pet == null){
            log.info("No such pet found with Id {}",petId);
            throw new NotFoundException("No such pet found");
        }
        Doctor doctor = (Doctor) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Doctor doctor = (Doctor) getPrincipalOrNull();
        Diagnosis diagnosis = new Diagnosis(doctor,pet,text);
        diagnosisService.persist(diagnosis);
        log.info("Added new diagnosis {}",text);
        return new ResponseEntity<>(diagnosisMapper.toDto(diagnosis),
                HttpStatus.CREATED);
    }

    @Operation(summary = "add treatment for diagnose")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added treatment ",
                    content = @Content(schema = @Schema(implementation = TreatmentDto.class))),
            @ApiResponse(responseCode = "404", description = "Unknown medicine"),
            @ApiResponse(responseCode = "404", description = "Unknown diagnoses")
    })

    @PostMapping("/diagnosis/{diagnoseId}/addTreatment")
    public ResponseEntity<TreatmentDto> addTreatment(@PathVariable Long diagnoseId,
                                                     @RequestBody List<AbstractNewProcedureDto> procedures){
        if (!diagnosisService.isExistByKey(diagnoseId)) {
            throw new NotFoundException("diagnoses with id = " + diagnoseId + " not found");
        }
        Diagnosis diagnosis = diagnosisService.getByKey(diagnoseId);
        log.info("Added new diagnosis Id {}",diagnosis.getId());
        List<Procedure> procedureList = abstractNewProcedureMapper.toEntity(procedures);
        procedureService.persistAll(procedureList);
        Treatment treatment = new Treatment();
        treatment.setProcedureList(procedureList);
        treatment.setDiagnosis(diagnosis);
        treatmentService.persist(treatment);
        log.info("Added new diagnosis treatment {}",treatment.getProcedureList().toString());
        return new ResponseEntity<>(treatmentMapper.toDto(treatment),HttpStatus.CREATED);
    }

}
