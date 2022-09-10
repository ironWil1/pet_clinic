package com.vet24.web.controllers.pet.clinicalexamination;

import static com.vet24.models.secutity.SecurityUtil.getOptionalOfNullableSecurityUser;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.dto.pet.clinicalexamination.ClinicalExaminationRequestDto;
import com.vet24.models.dto.pet.clinicalexamination.ClinicalExaminationResponseDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.pet.clinicalexamination.ClinicalExaminationRequestMapper;
import com.vet24.models.mappers.pet.clinicalexamination.ClinicalExaminationResponseMapper;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.clinicalexamination.ClinicalExamination;
import com.vet24.models.user.User;
import com.vet24.models.util.View;
import com.vet24.service.pet.PetService;
import com.vet24.service.pet.clinicalexamination.ClinicalExaminationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/doctor/exam")
@Tag(name = "Клиническое обследование")
public class ClinicalExaminationController {

    private static final String DESCRIPTION_OF_EXCEPTION = "clinical examination not found";
    private final PetService petService;
    private final ClinicalExaminationService clinicalExaminationService;
    private final ClinicalExaminationResponseMapper clinicalExaminationResponseMapper;
    private final ClinicalExaminationRequestMapper clinicalExaminationRequestMapper;

    public ClinicalExaminationController(PetService petService, ClinicalExaminationService clinicalExaminationService,
                                         ClinicalExaminationResponseMapper clinicalExaminationResponseMapper,
                                         ClinicalExaminationRequestMapper clinicalExaminationRequestMapper) {
        this.petService = petService;
        this.clinicalExaminationService = clinicalExaminationService;
        this.clinicalExaminationResponseMapper = clinicalExaminationResponseMapper;
        this.clinicalExaminationRequestMapper = clinicalExaminationRequestMapper;
    }

    @Operation(summary = "Получение клинических обследований по ID питомца",
            description = "Введите ID питомца")
    @ApiResponse(responseCode = "200", description = "Список клинических обследований питомца найден",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClinicalExaminationResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Указан неверный ID питомца",
            content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionDto.class)),
            description = "Клинические обследования с таким ID питомца не найдены")
    @GetMapping("")
    public ResponseEntity<List<ClinicalExaminationResponseDto>> getByPetId(@RequestParam(value = "petId") Long petId) {
        if (!petService.isExistByKey(petId)) {
            throw new BadRequestException("no pet with this id");
        }
        if (!clinicalExaminationService.isExistByPetId(petId)) {
            throw new NotFoundException(DESCRIPTION_OF_EXCEPTION);
        }
        List<ClinicalExamination> clinicalExaminations = clinicalExaminationService.getByPetId(petId);
        return new ResponseEntity<>(clinicalExaminationResponseMapper.toDto(clinicalExaminations), HttpStatus.OK);
    }

    @Operation(summary = "Получение клинического обследования по ID",
            description = "Введите ID клинического обследования")
    @ApiResponse(responseCode = "200", description = "Клиническое обследование найдено",
            content = @Content(schema = @Schema(implementation = ClinicalExaminationResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Указан некорректный запрос",
            content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    @ApiResponse(responseCode = "404", description = "Клиническое обследование с этим идентификатором не найдено",
            content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    @GetMapping("/{examId}")
    public ResponseEntity<ClinicalExaminationResponseDto> getById(@PathVariable Long examId) {
        Optional<User> doctor = getOptionalOfNullableSecurityUser();
        ClinicalExamination clinicalExamination = clinicalExaminationService.getByKey(examId);
        if (clinicalExamination == null) {
            throw new NotFoundException(DESCRIPTION_OF_EXCEPTION);
        }
        return new ResponseEntity<>(clinicalExaminationResponseMapper.toDto(clinicalExamination), HttpStatus.OK);
    }

    @Operation(summary = "Добавить новое клиническое обследование",
            description = "Чтобы добавить новое клиническое обследование - введите ID питомца и заполните поля: " +
                    "wight, isCanMove, text")
    @ApiResponse(responseCode = "201", description = "Клиническое обследование успешно добавлено",
            content = @Content(schema = @Schema(implementation = ClinicalExaminationResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Питомец с таким ID не найден",
            content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    @PostMapping("")
    public ResponseEntity<ClinicalExaminationResponseDto> save(
            @RequestBody ClinicalExaminationRequestDto clinicalExamRequestDto,
            @RequestParam(value = "petId") Long petId) {
        Pet pet = petService.getByKey(petId);
        if (pet == null) {
            throw new NotFoundException("pet not found");
        }
        ClinicalExamination clinicalExamination = clinicalExaminationRequestMapper.toEntity(clinicalExamRequestDto);
        clinicalExamination.setDate(LocalDate.now());
        pet.setWeight(clinicalExamination.getWeight());
        pet.addClinicalExamination(clinicalExamination);
        clinicalExamination.setPet(pet);
        clinicalExaminationService.persist(clinicalExamination);
        petService.update(pet);

        return new ResponseEntity<>(clinicalExaminationResponseMapper.toDto(clinicalExamination), HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить клиническое обследование по ID",
            description = "Чтобы обновить клиническое обследование - введите ID и заполните поля: " +
                    "wight, isCanMove, text")
    @ApiResponse(responseCode = "200", description = "Клиническое обследование успешно обновлено")
    @ApiResponse(responseCode = "404", description = "Клиническое обследование с этим ID не найдено",
            content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    @ApiResponse(responseCode = "400", description = "Клиническое обследование не назначено этому питомцу",
            content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    @PutMapping("/{examId}")
    public ResponseEntity<Void> update(@PathVariable Long examId,
                                       @JsonView(View.Put.class)
                                       @RequestBody
                                       ClinicalExaminationRequestDto clinicalExamRequestDto) {
        Optional<User> doctor = getOptionalOfNullableSecurityUser();
        ClinicalExamination clinicalExamination = clinicalExaminationService.getById(examId);
        Pet pet = clinicalExamination.getPet();
        if (!petService.isExistByPetIdAndClientId(pet.getId(), doctor.get().getId())) {
            throw new BadRequestException("clinical examination not assigned to this pet");
        }

        clinicalExamination.setDoctor(doctor.get());
        clinicalExamination.setDate(LocalDate.now());
        pet.setWeight(clinicalExamRequestDto.getWeight());
        clinicalExamination.setPet(pet);
        clinicalExaminationRequestMapper.updateEntity(clinicalExamRequestDto, clinicalExamination);
        petService.update(pet);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Operation(summary = "Удалить клиническое обследование по ID",
            description = "Введите ID клинического " + "обследования питомца")
    @ApiResponse(responseCode = "200", description = "Клиническое обследование успешно удалено")
    @ApiResponse(responseCode = "404", description = "Клиническое обследование не найдено",
            content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    @DeleteMapping(value = "/{examId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long examId) {
        Optional<User> doctor = getOptionalOfNullableSecurityUser();
        ClinicalExamination clinicalExamination = clinicalExaminationService.getByKey(examId);
        if (clinicalExamination == null) {
            throw new NotFoundException(DESCRIPTION_OF_EXCEPTION);
        }
        clinicalExaminationService.delete(clinicalExamination);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}