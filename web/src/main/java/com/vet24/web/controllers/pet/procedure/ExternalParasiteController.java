package com.vet24.web.controllers.pet.procedure;

import com.vet24.models.dto.OnUpdate;
import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.dto.pet.procedure.ExternalParasiteDto;
import com.vet24.models.enums.ProcedureType;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.pet.procedure.ExternalParasiteMapper;
import com.vet24.models.medicine.Medicine;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.procedure.ExternalParasiteProcedure;
import com.vet24.models.pet.procedure.Procedure;
import com.vet24.models.user.Client;
import com.vet24.service.medicine.MedicineService;
import com.vet24.service.pet.PetService;
import com.vet24.service.pet.procedure.ExternalParasiteProcedureService;
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
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.vet24.models.secutity.SecurityUtil.getSecurityUserOrNull;

@RestController
@Slf4j
@RequestMapping("api/client/procedure/external")
@Tag(name = "external parasite-controller", description = "operations with external parasite procedure")
public class ExternalParasiteController {
    private final PetService petService;
    private final ExternalParasiteProcedureService externalParasiteProcedureService;
    private final MedicineService medicineService;
    private final ExternalParasiteMapper externalParasiteMapper;

    private static final String PET_NOT_FOUND = "pet not found";
    private static final String PROCEDURE_NOT_FOUND = "procedure not found";
    private static final String NOT_YOURS = "pet not yours";

    //TODO add validationUtil
    private void checkPet(Long petId) {
        if (petService.getByKey(petId) == null) {
            log.info("The pet with this id {} was not found", petId);
            throw new NotFoundException(PET_NOT_FOUND);
        }
    }

    //TODO add validationUtil
    private void checkProcedure(Long id) {
        if (externalParasiteProcedureService.getByKey(id) == null) {
            log.info("The procedure with this id {} was not found", id);
            throw new NotFoundException(PROCEDURE_NOT_FOUND);
        }
    }

    //TODO add validationUtil
    private void checkPetOwner(Long petId) {
        if (!petService.getByKey(petId).getClient().getId().equals(getSecurityUserOrNull().getId())) {
            log.info("The pet with this id {} is not yours", petId);
            throw new BadRequestException(NOT_YOURS);
        }
    }

    @Autowired
    public ExternalParasiteController(PetService petService, MedicineService medicineService,
                                      ExternalParasiteMapper externalParasiteMapper,
                                      ExternalParasiteProcedureService externalParasiteProcedureService) {
        this.petService = petService;
        this.externalParasiteProcedureService = externalParasiteProcedureService;
        this.medicineService = medicineService;
        this.externalParasiteMapper = externalParasiteMapper;
    }

    @Operation(summary = "get an external parasite procedures")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully get a external parasite procedures",
                    content = @Content(schema = @Schema(implementation = ExternalParasiteDto.class))),
            @ApiResponse(responseCode = "404", description = "Procedures not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not assigned with external parasite procedures",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<ExternalParasiteDto>> get(@RequestParam Long petId) {
        checkPet(petId);
        checkPetOwner(petId);

        Pet pet = petService.getByKey(petId);
        List<ExternalParasiteProcedure> externalParasiteProcedureList = pet.getProcedures()
                .stream()
                .filter(procedure -> procedure instanceof ExternalParasiteProcedure)
                .map(procedure -> (ExternalParasiteProcedure) procedure)
                .collect(Collectors.toList());

        if (externalParasiteProcedureList.isEmpty()) {
            log.info("The external parasite procedures was not found");
            throw new NotFoundException(PROCEDURE_NOT_FOUND);
        }

        List<ExternalParasiteDto> externalParasiteDtoList = externalParasiteMapper.toDto(externalParasiteProcedureList);
        externalParasiteProcedureList.forEach(externalParasiteProcedure ->
                log.info("We have this procedure {}", externalParasiteProcedure.getId()));

        return new ResponseEntity<>(externalParasiteDtoList, HttpStatus.OK);
    }

    @Operation(summary = "get an external parasite procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully get a external parasite procedure",
                    content = @Content(schema = @Schema(implementation = ExternalParasiteDto.class))),
            @ApiResponse(responseCode = "404", description = "External parasite procedure not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not assigned with external parasite procedure",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExternalParasiteDto> getById(@PathVariable Long id) {
        checkProcedure(id);

        ExternalParasiteProcedure externalParasiteProcedure = externalParasiteProcedureService.getByKey(id);

        ExternalParasiteDto externalParasiteDto = externalParasiteMapper.toDto(externalParasiteProcedure);
        log.info("We have this procedure {}", id);
        return new ResponseEntity<>(externalParasiteDto, HttpStatus.OK);
    }

    @Operation(summary = "add a new external parasite procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added a new external parasite procedure",
                    content = @Content(schema = @Schema(implementation = ExternalParasiteDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet is not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not yours",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PostMapping("")
    public ResponseEntity<ExternalParasiteDto> save(@RequestParam Long petId,
                                                    @RequestBody ExternalParasiteDto externalParasiteDto) {
        checkPet(petId);
        checkPetOwner(petId);

        ExternalParasiteProcedure externalParasiteProcedure = externalParasiteMapper.toEntity(externalParasiteDto);
        Pet pet = petService.getByKey(petId);

        Medicine medicine = medicineService.getByKey(externalParasiteDto.getMedicineId());
        externalParasiteProcedure.setMedicine(medicine);
        externalParasiteProcedure.setPet(pet);
        externalParasiteProcedureService.persist(externalParasiteProcedure);

        pet.addProcedure(externalParasiteProcedure);
        petService.update(pet);
        log.info("We added procedure with this id {}", externalParasiteDto.getMedicineId());
        return new ResponseEntity<>(externalParasiteMapper.toDto(externalParasiteProcedure), HttpStatus.CREATED);
    }

    @Operation(summary = "update an external parasite procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated a external parasite procedure",
                    content = @Content(schema = @Schema(implementation = ExternalParasiteDto.class))),
            @ApiResponse(responseCode = "404", description = "External parasite procedure is not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not assigned with external parasite procedure",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExternalParasiteDto> update(@PathVariable Long id,
                                                      @Validated(OnUpdate.class)
                                                      @RequestBody ExternalParasiteDto externalParasiteDto) {
        checkProcedure(id);

        ExternalParasiteProcedure externalParasiteProcedure = externalParasiteProcedureService.getByKey(id);
        Pet pet = externalParasiteProcedure.getPet();

        checkPet(pet.getId());
        checkPetOwner(pet.getId());

        externalParasiteMapper.updateEntity(externalParasiteDto, externalParasiteProcedure);
        Medicine medicine = medicineService.getByKey(externalParasiteDto.getMedicineId());
        externalParasiteProcedure.setMedicine(medicine);
        externalParasiteProcedureService.update(externalParasiteProcedure);
        log.info("We updated procedure with this id {}", externalParasiteProcedure.getId());

        return new ResponseEntity<>(externalParasiteMapper.toDto(externalParasiteProcedure), HttpStatus.OK);
    }

    @Operation(summary = "delete an external parasite procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted a external parasite procedure"),
            @ApiResponse(responseCode = "404", description = "External parasite procedure is not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not assigned with external parasite procedure",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        checkProcedure(id);

        ExternalParasiteProcedure externalParasiteProcedure = externalParasiteProcedureService.getByKey(id);
        Pet pet = externalParasiteProcedure.getPet();

        checkPet(pet.getId());
        checkPetOwner(pet.getId());

        externalParasiteProcedureService.delete(externalParasiteProcedure);
        pet.removeProcedure(externalParasiteProcedure);
        petService.update(pet);
        log.info("We deleted procedure with this id {}", externalParasiteProcedure.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
