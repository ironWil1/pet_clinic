package com.vet24.web.controllers.pet.procedure;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.dto.pet.procedure.VaccinationDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.pet.procedure.VaccinationMapper;
import com.vet24.models.medicine.Medicine;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.procedure.VaccinationProcedure;
import com.vet24.models.user.User;
import com.vet24.models.util.View;
import com.vet24.service.medicine.MedicineService;
import com.vet24.service.pet.PetService;
import com.vet24.service.pet.procedure.VaccinationProcedureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

import java.util.List;

import static com.vet24.models.secutity.SecurityUtil.getOptionalOfNullableSecurityUser;

@RestController
@Slf4j
@RequestMapping("api/client/procedure/vaccination")
@Tag(name = "vaccination-controller", description = "operations with vaccination procedure")
public class VaccinationController {
    private static final String PET_NOT_FOUND = "pet not found";
    private static final String PROCEDURE_NOT_FOUND = "procedure not found";
    private static final String NOT_YOURS = "pet not yours";
    private final PetService petService;
    private final VaccinationProcedureService vaccinationProcedureService;
    private final MedicineService medicineService;
    private final VaccinationMapper vaccinationMapper;

    @Autowired
    public VaccinationController(PetService petService, MedicineService medicineService,
                                 VaccinationMapper vaccinationMapper,
                                 VaccinationProcedureService vaccinationProcedureService) {
        this.petService = petService;
        this.vaccinationProcedureService = vaccinationProcedureService;
        this.medicineService = medicineService;
        this.vaccinationMapper = vaccinationMapper;
    }

    private void checkPet(Long petId) {
        if (!petService.isExistByKey(petId)) {
            log.info("The pet with this id {} was not found", petId);
            throw new NotFoundException(PET_NOT_FOUND);
        }
    }

    private void checkProcedure(Long id) {
        if (!vaccinationProcedureService.isExistByKey(id)) {
            log.info("The procedure with this id {} was not found", id);
            throw new NotFoundException(PROCEDURE_NOT_FOUND);
        }
    }

    private void checkOwnerPet(Long petId) {
        getOptionalOfNullableSecurityUser()
                .map(User::getId)
                .filter(userId -> petService.isExistByPetIdAndClientId(petId, userId))
                .orElseThrow(() -> {
                    log.info("The pet with this id {} is not yours", petId);
                    throw new BadRequestException(NOT_YOURS);
                });
    }

    @Operation(summary = "получить все процедуры вакцинации по id питомца")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully get a Procedure",
                    content = @Content(schema = @Schema(implementation = VaccinationDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet or Procedure not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not assigned with Procedure OR pet not yours",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping()
    public ResponseEntity<List<VaccinationDto>> getByPetId(@RequestParam(name = "petId") Long petId) {

        checkPet(petId);
        checkOwnerPet(petId);
        List<VaccinationProcedure> vaccinationProcedureList = petService.getByKey(petId).getVaccinationProcedures();

        if (vaccinationProcedureList.isEmpty()) {
            throw new NotFoundException(PROCEDURE_NOT_FOUND);
        }


        List<VaccinationDto> vaccinationDtoList = vaccinationMapper.toDto(vaccinationProcedureList);


        return new ResponseEntity<>(vaccinationDtoList, HttpStatus.OK);
    }

    @Operation(summary = "получить процедуру вакцинации по id процедуры")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully get a Procedure",
                    content = @Content(schema = @Schema(implementation = VaccinationDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet or Procedure not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not assigned with Procedure OR pet not yours",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })

    @GetMapping("/{id}")
    public ResponseEntity<VaccinationDto> getByProcedureId(@PathVariable Long id) {
        checkProcedure(id);
        VaccinationProcedure vaccinationProcedure = vaccinationProcedureService.getByKey(id);
        checkPet(vaccinationProcedure.getPet().getId());
        checkOwnerPet(vaccinationProcedure.getPet().getId());
        VaccinationDto vaccinationDto = vaccinationMapper.toDto(vaccinationProcedure);

        return new ResponseEntity<>(vaccinationDto, HttpStatus.OK);
    }

    @Operation(summary = "добавить процедуру вакцинации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully save a Procedure",
                    content = @Content(schema = @Schema(implementation = VaccinationDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not yours",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PostMapping()
    public ResponseEntity<VaccinationDto> save(@RequestParam(name = "petId") Long petId,
                                               @JsonView(View.Post.class)
                                               @Validated(OnCreate.class)
                                               @RequestBody VaccinationDto vaccinationDto) {
        checkPet(petId);
        checkOwnerPet(petId);

        VaccinationProcedure vaccinationProcedure = vaccinationMapper.toEntity(vaccinationDto);

        Medicine medicine = medicineService.getByKey(vaccinationDto.getMedicineId());
        vaccinationProcedure.setPet(petService.getByKey(petId));
        vaccinationProcedure.setMedicine(medicine);
        vaccinationProcedure.setMedicineBatchNumber(vaccinationDto.getMedicineBatchNumber());
        vaccinationProcedure.setIsPeriodical(vaccinationDto.getIsPeriodical());
        vaccinationProcedure.setPeriodDays(vaccinationDto.getPeriodDays());
        vaccinationProcedureService.persist(vaccinationProcedure);

        petService.getByKey(petId).addVaccinationProcedure(vaccinationProcedure);
        petService.update(petService.getByKey(petId));

        log.info("We added vaccination procedure with this id {}", vaccinationDto.getMedicineId());

        return new ResponseEntity<>(vaccinationDto, HttpStatus.CREATED);

    }

    @Operation(summary = "обновить процедуру вакцинации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully save a Procedure",
                    content = @Content(schema = @Schema(implementation = VaccinationDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet or Procedure not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not assigned with Procedure OR pet not yours",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<VaccinationDto> update(@PathVariable Long id,
                                                 @RequestBody VaccinationDto vaccinationDto) {
        checkProcedure(id);

        VaccinationProcedure vaccinationProcedure = vaccinationProcedureService.getByKey(id);

        checkProcedure(vaccinationProcedure.getId());
        checkOwnerPet(vaccinationProcedure.getPet().getId());

        Pet pet = vaccinationProcedure.getPet();
        Long petId = vaccinationProcedure.getPet().getId();

        Medicine medicine = medicineService.getByKey(vaccinationDto.getMedicineId());
        vaccinationProcedure.setPet(petService.getByKey(petId));
        vaccinationProcedure.setMedicine(medicine);
        vaccinationProcedure.setMedicineBatchNumber(vaccinationDto.getMedicineBatchNumber());
        vaccinationProcedure.setIsPeriodical(vaccinationDto.getIsPeriodical());
        vaccinationProcedure.setPeriodDays(vaccinationDto.getPeriodDays());
        vaccinationProcedureService.update(vaccinationProcedure);

        petService.getByKey(petId).addVaccinationProcedure(vaccinationProcedure);
        petService.update(pet);


        return new ResponseEntity<>(vaccinationDto, HttpStatus.OK);
    }

    @Operation(summary = "удалить процедуру вакцинации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully delete a Procedure",
                    content = @Content(schema = @Schema(implementation = VaccinationDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet or Procedure not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not assigned with Procedure OR pet not yours",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<VaccinationDto> delete(@PathVariable Long id) {
        checkProcedure(id);
        VaccinationProcedure vaccinationProcedure = vaccinationProcedureService.getByKey(id);
        Pet pet = vaccinationProcedure.getPet();

        checkProcedure(vaccinationProcedure.getId());
        checkOwnerPet(vaccinationProcedure.getPet().getId());
        vaccinationProcedureService.delete(vaccinationProcedure);
        pet.removeVaccinationProcedure(vaccinationProcedure);
        petService.update(pet);

        log.info("We deleted procedure with this id {}", vaccinationProcedure.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
