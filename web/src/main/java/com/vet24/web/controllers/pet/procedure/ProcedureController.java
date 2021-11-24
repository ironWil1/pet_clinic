package com.vet24.web.controllers.pet.procedure;

import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.dto.pet.procedure.AbstractNewProcedureDto;
import com.vet24.models.dto.pet.procedure.ProcedureDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.pet.procedure.AbstractNewProcedureMapper;
import com.vet24.models.mappers.pet.procedure.ProcedureMapper;
import com.vet24.models.medicine.Medicine;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.procedure.Procedure;
import com.vet24.models.user.Client;
import com.vet24.service.medicine.MedicineService;
import com.vet24.service.pet.PetService;
import com.vet24.service.pet.procedure.ProcedureService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

@RestController
@Slf4j
@RequestMapping("api/client/pet/{petId}/procedure")
@Tag(name = "procedure-controller", description = "operations with Procedures")
public class ProcedureController {

    private final PetService petService;
    private final ProcedureService procedureService;
    private final ProcedureMapper procedureMapper;
    private final AbstractNewProcedureMapper newProcedureMapper;
    private final MedicineService medicineService;

    private static final String PET_NOT_FOUND = "pet not found";
    private static final String PROCEDURE_NOT_FOUND = "procedure not found";
    private static final String NOT_YOURS = "pet not yours";
    private static final String NOT_ASSIGNED = "pet not assigned to this procedure";

    @Autowired
    public ProcedureController(PetService petService, ProcedureService procedureService,
                               ProcedureMapper procedureMapper, AbstractNewProcedureMapper newProcedureMapper,
                               MedicineService medicineService) {
        this.petService = petService;
        this.procedureService = procedureService;
        this.procedureMapper = procedureMapper;
        this.newProcedureMapper = newProcedureMapper;
        this.medicineService = medicineService;
    }

    @Operation(summary = "get a Procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully get a Procedure",
                    content = @Content(schema = @Schema(implementation = ProcedureDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet or Procedure not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not assigned with Procedure OR pet not yours",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping("/{procedureId}")
    public ResponseEntity<ProcedureDto> getById(@PathVariable Long petId, @PathVariable Long procedureId) {
        Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pet pet = petService.getByKey(petId);
        Procedure procedure = procedureService.getByKey(procedureId);

        if (pet == null) {
            log.info("The pet with this id {} was not found",petId);
            throw new NotFoundException(PET_NOT_FOUND);
        }
        if (procedure == null) {
            log.info("The procedure with this id {} was not found",procedureId);
            throw new NotFoundException(PROCEDURE_NOT_FOUND);
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            log.info("The pet with this id {} is not yours",petId);
            throw new BadRequestException(NOT_YOURS);
        }
        if (!procedure.getPet().getId().equals(pet.getId())) {
            log.info("The pet with this id {}  not assigned to this procedure {}",petId,procedure.getPet().getId());
            throw new BadRequestException(NOT_ASSIGNED);
        }
        ProcedureDto procedureDto = procedureMapper.toDto(procedure);
        log.info("We have this procedure {}",procedureId);
        return new ResponseEntity<>(procedureDto, HttpStatus.OK);
    }

    @Operation(summary = "add a new Procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added a new Procedure",
                    content = @Content(schema = @Schema(implementation = ProcedureDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet is not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "pet not yours",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PostMapping("")
    public ResponseEntity<ProcedureDto> save(@PathVariable Long petId, @Validated(OnCreate.class)
                                             @RequestBody AbstractNewProcedureDto newProcedureDto) {

        Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pet pet = petService.getByKey(petId);
        Procedure procedure = newProcedureMapper.toEntity(newProcedureDto);

        if (pet == null) {
            throw new NotFoundException(PET_NOT_FOUND);
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            throw new BadRequestException(NOT_YOURS);
        }

        Medicine medicine = medicineService.getByKey(newProcedureDto.getMedicineId());
        procedure.setMedicine(medicine);
        procedure.setPet(pet);
        procedureService.persist(procedure);

        pet.addProcedure(procedure);
        petService.update(pet);
        log.info("We added procedure with this id {}",newProcedureDto.getMedicineId());
        return new ResponseEntity<>(procedureMapper.toDto(procedure), HttpStatus.CREATED);
    }

    @Operation(summary = "update a Procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated a Procedure",
                    content = @Content(schema = @Schema(implementation = ProcedureDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet or Procedure is not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not assigned with Procedure OR pet not yours",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PutMapping("/{procedureId}")
    public ResponseEntity<ProcedureDto> update(@PathVariable Long petId, @PathVariable Long procedureId,
                                         @Validated(OnUpdate.class )@RequestBody ProcedureDto procedureDto) {

        Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pet pet = petService.getByKey(petId);
        Procedure procedure = procedureService.getByKey(procedureId);

        if (pet == null) {
            throw new NotFoundException(PET_NOT_FOUND);
        }
        if (procedure == null) {
            throw new NotFoundException(PROCEDURE_NOT_FOUND);
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            throw new BadRequestException(NOT_YOURS);
        }
        if (!procedure.getPet().getId().equals(pet.getId())) {
            throw new BadRequestException(NOT_ASSIGNED);
        }
        if (!procedureDto.getId().equals(procedureId)) {
            throw new BadRequestException("procedureId in path and in body not equals");
        }
        procedure = procedureMapper.toEntity(procedureDto);
        Medicine medicine = medicineService.getByKey(procedureDto.getMedicineId());
        procedure.setMedicine(medicine);
        procedure.setPet(pet);
        procedureService.update(procedure);
        log.info("We updated procedure with this id {}",procedure.getId());

        return new ResponseEntity<>(procedureMapper.toDto(procedure), HttpStatus.OK);
    }

    @Operation(summary = "delete a Procedure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted a Procedure"),
            @ApiResponse(responseCode = "404", description = "Pet or Procedure is not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not assigned with Procedure OR pet not yours",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @DeleteMapping("/{procedureId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long petId, @PathVariable Long procedureId) {
        Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Procedure procedure = procedureService.getByKey(procedureId);
        Pet pet = petService.getByKey(petId);

        if (pet == null) {
            throw new NotFoundException(PET_NOT_FOUND);
        }
        if (procedure == null) {
            throw new NotFoundException(PROCEDURE_NOT_FOUND);
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            throw new BadRequestException(NOT_YOURS);
        }
        if (!procedure.getPet().getId().equals(pet.getId())) {
            throw new BadRequestException(NOT_ASSIGNED);
        }

        procedureService.delete(procedure);
        pet.removeProcedure(procedure);
        petService.update(pet);
        log.info("We deleted procedure with this id {}",procedure.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
