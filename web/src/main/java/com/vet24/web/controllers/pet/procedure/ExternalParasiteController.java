package com.vet24.web.controllers.pet.procedure;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.dto.pet.procedure.ExternalParasiteDto;
import com.vet24.models.dto.pet.procedure.ProcedureDto;
import com.vet24.models.enums.ProcedureType;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.pet.procedure.ExternalParasiteMapper;
import com.vet24.models.medicine.Medicine;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.procedure.ExternalParasiteProcedure;
import com.vet24.models.pet.procedure.Procedure;
import com.vet24.models.user.Client;
import com.vet24.models.util.View;
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
    private static final String NOT_ASSIGNED = "pet not assigned to this procedure";

    @Autowired
    public ExternalParasiteController(PetService petService, MedicineService medicineService,
                                      ExternalParasiteMapper externalParasiteMapper,
                                      ExternalParasiteProcedureService externalParasiteProcedureService) {
        this.petService = petService;
        this.externalParasiteProcedureService = externalParasiteProcedureService;
        this.medicineService = medicineService;
        this.externalParasiteMapper = externalParasiteMapper;
    }

    @Operation(summary = "get an external parasite procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully get a external parasite procedure",
                    content = @Content(schema = @Schema(implementation = ExternalParasiteDto.class))),
            @ApiResponse(responseCode = "404", description = "Procedure not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not assigned with external parasite procedure",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping
    public ResponseEntity<ExternalParasiteDto> get(@RequestParam Long petId) {
        Client client = (Client) getSecurityUserOrNull();
        Pet pet = petService.getByKey(petId);

        if (pet == null) {
            log.info("The pet with this id {} was not found", petId);
            throw new NotFoundException(PET_NOT_FOUND);
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            log.info("The pet with this id {} is not yours", petId);
            throw new BadRequestException(NOT_YOURS);
        }

        ExternalParasiteProcedure externalParasiteProcedure = (ExternalParasiteProcedure) pet.getProcedures()
                .stream()
                .filter(p -> (p instanceof ExternalParasiteProcedure))
                .findFirst()
                .orElse(null);

        if (externalParasiteProcedure == null) {
            log.info("The external parasite procedure was not found");
            throw new NotFoundException(PROCEDURE_NOT_FOUND);
        }
        if (!externalParasiteProcedure.getPet().getId().equals(pet.getId())) {
            log.info("The pet with this id {}  not assigned to this procedure {}", petId,
                    externalParasiteProcedure.getPet().getId());
            throw new BadRequestException(NOT_ASSIGNED);
        }

        ExternalParasiteDto externalParasiteDto = externalParasiteMapper.toDto(externalParasiteProcedure);
        log.info("We have this procedure {}", externalParasiteProcedure.getId());
        return new ResponseEntity<>(externalParasiteDto, HttpStatus.OK);
    }

    @Operation(summary = "get an external parasite procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully get a external parasite procedure",
                    content = @Content(schema = @Schema(implementation = ExternalParasiteDto.class))),
            @ApiResponse(responseCode = "404", description = "Procedure not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not assigned with external parasite procedure",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExternalParasiteDto> getById(@PathVariable Long id) {
        ExternalParasiteProcedure externalParasiteProcedure = externalParasiteProcedureService.getByKey(id);

        if (externalParasiteProcedure == null) {
            log.info("The procedure with this id {} was not found",id);
            throw new NotFoundException(PROCEDURE_NOT_FOUND);
        }

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

        Client client = (Client) getSecurityUserOrNull();
        Pet pet = petService.getByKey(petId);
        ExternalParasiteProcedure externalParasiteProcedure = externalParasiteMapper.toEntity(externalParasiteDto);

        if (pet == null) {
            throw new NotFoundException(PET_NOT_FOUND);
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            throw new BadRequestException(NOT_YOURS);
        }

        Medicine medicine = medicineService.getByKey(externalParasiteDto.getMedicineId());
        externalParasiteProcedure.setMedicine(medicine);
        externalParasiteProcedure.setPet(pet);
//        externalParasiteProcedure.setType(ProcedureType.EXTERNAL_PARASITE);
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
//                                                      @JsonView(View.Put.class)
                                                      @Validated(OnUpdate.class)
                                                      @RequestBody ExternalParasiteDto externalParasiteDto) {

        ExternalParasiteProcedure externalParasiteProcedure = externalParasiteProcedureService.getByKey(id);

        if (externalParasiteProcedure == null) {
            throw new NotFoundException(PROCEDURE_NOT_FOUND);
        }

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

        ExternalParasiteProcedure externalParasiteProcedure = externalParasiteProcedureService.getByKey(id);

        if (externalParasiteProcedure == null) {
            throw new NotFoundException(PROCEDURE_NOT_FOUND);
        }

        externalParasiteProcedureService.delete(externalParasiteProcedure);
        log.info("We deleted procedure with this id {}", externalParasiteProcedure.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
