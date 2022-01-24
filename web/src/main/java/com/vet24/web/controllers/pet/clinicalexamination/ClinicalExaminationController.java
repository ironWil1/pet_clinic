package com.vet24.web.controllers.pet.clinicalexamination;

import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.dto.pet.clinicalexamination.ClinicalExaminationDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.pet.clinicalexamination.ClinicalExaminationMapper;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.clinicalexamination.ClinicalExamination;
import com.vet24.models.user.Doctor;
import com.vet24.service.pet.PetService;
import com.vet24.service.pet.clinicalexamination.ClinicalExaminationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/doctor/exam")
public class ClinicalExaminationController {

    private final PetService petService;
    private final ClinicalExaminationService clinicalExaminationService;
    private final ClinicalExaminationMapper clinicalExaminationMapper;

    private static final String DESCRIPTION_OF_EXCEPTION = "clinical examination not found";

    public ClinicalExaminationController(PetService petService, ClinicalExaminationService clinicalExaminationService,
                                         ClinicalExaminationMapper clinicalExaminationMapper) {
        this.petService = petService;
        this.clinicalExaminationService = clinicalExaminationService;
        this.clinicalExaminationMapper = clinicalExaminationMapper;
    }

    @Operation(
            summary = "get clinical examination by id",
            description = "is looking for one clinical examination for a unique identifier")
    @ApiResponse(responseCode = "200", description = "ok",
            content = @Content(schema = @Schema(implementation = ClinicalExaminationDto.class)))
    @ApiResponse(responseCode = "400", description = "incorrect query input is specified",
            content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    @ApiResponse(responseCode = "404", description = "clinical examination or pet with " +
            "this id not found",
            content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    @GetMapping("/{examinationId}")
    public ResponseEntity<ClinicalExaminationDto> getById(@PathVariable Long examinationId) {
        ClinicalExamination clinicalExamination = clinicalExaminationService.getByKey(examinationId);

        if (clinicalExamination == null) {
            throw new NotFoundException(DESCRIPTION_OF_EXCEPTION);
        }

        ClinicalExaminationDto clinicalExaminationDto =
                clinicalExaminationMapper.toDto(clinicalExamination);
        return new ResponseEntity<>(clinicalExaminationDto, HttpStatus.OK);
    }

    @Operation(
            summary = "add new clinical examination",
            description = "to add a new clinical exam, enter the pet ID and fill in the fields: wight, isCanMove, text.")
    @ApiResponse(responseCode = "201", description = "clinical examination successful " +
            "created",
            content = @Content(schema = @Schema(implementation = ClinicalExaminationDto.class)))
    @ApiResponse(responseCode = "404", description = "pet with this id not found",
            content = @Content(schema = @Schema(implementation = ExceptionDto.class)))

    @PostMapping("")
    public ResponseEntity<ClinicalExaminationDto> save(@RequestBody ClinicalExaminationDto clinicalExaminationDto) {
        Pet pet = petService.getByKey(clinicalExaminationDto.getPetId());
        ClinicalExamination clinicalExamination =
                clinicalExaminationMapper.toEntity(clinicalExaminationDto);
        if (pet == null) {
            throw new NotFoundException("pet not found");
        }
        clinicalExamination.setId(null);
        clinicalExamination.setDoctor((Doctor) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        clinicalExamination.setDate(LocalDate.now());

        clinicalExaminationService.persist(clinicalExamination);
        pet.setWeight(clinicalExamination.getWeight());
        clinicalExamination.setPet(petService.getByKey(clinicalExaminationDto.getPetId()));

        pet.addClinicalExamination(clinicalExamination);
        petService.update(pet);
        return new ResponseEntity<>(clinicalExaminationMapper.toDto(clinicalExamination), HttpStatus.CREATED);
    }

    @Operation(
            summary = "update clinical examination by id",
            description = "enter pet ID and clinical exam ID")
    @ApiResponse(responseCode = "200", description = "clinical examination successful " +
            "updated",
            content = @Content(schema = @Schema(implementation = ClinicalExaminationDto.class)))
    @ApiResponse(responseCode = "404", description = "clinical examination or pet with " +
            "this id not found",
            content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    @ApiResponse(responseCode = "400", description = "clinical examination not assigned " +
            "to this pet OR \n" +
            "examinationId in path and in body not equals OR \npet has no doctor",
            content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    @PutMapping("/{examinationId}")
    public ResponseEntity<ClinicalExaminationDto> update(@PathVariable Long examinationId,
                                                         @RequestBody ClinicalExaminationDto clinicalExaminationDto) {
        Pet pet = petService.getByKey(clinicalExaminationDto.getPetId());
        ClinicalExamination clinicalExamination = clinicalExaminationService.getByKey(examinationId);
        if (pet == null) {
            throw new NotFoundException("pet not found");
        }
        Doctor doctor = (Doctor) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (doctor == null) {
            throw new NotFoundException("there is no doctor assigned to this pet");
        }

        if (clinicalExamination == null) {
            throw new NotFoundException(DESCRIPTION_OF_EXCEPTION);
        }

        if (!clinicalExamination.getPet().getId().equals(pet.getId())) {
            throw new BadRequestException("clinical examination not assigned to this pet");
        }

        clinicalExamination.setDoctor(doctor);
        clinicalExamination.setDate(LocalDate.now());
        pet.setWeight(clinicalExaminationDto.getWeight());
        clinicalExamination.setDate(LocalDate.now());
        clinicalExamination.setPet(pet);
        clinicalExamination.setId(examinationId);
        clinicalExamination.setIsCanMove(clinicalExaminationDto.getIsCanMove());
        clinicalExamination.setText(clinicalExaminationDto.getText());
        clinicalExaminationService.update(clinicalExamination);
//        clinicalExamination =
//                clinicalExaminationMapper.toEntity(clinicalExaminationDto);


        return new ResponseEntity<>(clinicalExaminationMapper.toDto(clinicalExamination), HttpStatus.OK);
    }


    @Operation(
            summary = "delete clinical examination by id",
            description = "enter a unique ID of the pet's clinical examination")
    @ApiResponse(responseCode = "200", description = "clinical examination successful deleted")
    @ApiResponse(responseCode = "404", description = "clinical examination not found",
            content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    @DeleteMapping(value = "/{examinationId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long examinationId) {

        ClinicalExamination clinicalExamination = clinicalExaminationService.getByKey(examinationId);

        if (clinicalExamination == null) {
            throw new NotFoundException(DESCRIPTION_OF_EXCEPTION);
        }
        clinicalExaminationService.delete(clinicalExamination);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}