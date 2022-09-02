package com.vet24.web.controllers.pet.reproduction;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.dto.pet.reproduction.ReproductionDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.pet.reproduction.ReproductionMapper;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.reproduction.Reproduction;
import com.vet24.models.user.User;
import com.vet24.models.util.View;
import com.vet24.service.pet.PetService;
import com.vet24.service.pet.reproduction.ReproductionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.function.Function;

import static com.vet24.models.secutity.SecurityUtil.getOptionalOfNullableSecurityUser;


@RestController
@Slf4j
@RequestMapping("/api/client/pet/{petId}/reproduction")
public class ReproductionController {

    private final PetService petService;
    private final ReproductionService reproductionService;
    private final ReproductionMapper reproductionMapper;

    private static final String PET_NOT_FOUND = "pet not found";
    private static final String REPRODUCTION_NOT_FOUND = "reproduction not found";
    private static final String NOT_YOURS = "pet not yours";
    private static final String NOT_ASSIGNED = "reproduction not assigned to this pet";

    @Autowired
    public ReproductionController(ReproductionService reproductionService, ReproductionMapper reproductionMapper,
                                  PetService petService) {
        this.reproductionService = reproductionService;
        this.reproductionMapper = reproductionMapper;
        this.petService = petService;
    }

    @Operation(summary = "get all reproduction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all reproductions"),
            @ApiResponse(responseCode = "404", description = "Client or Pet or Pet's reproductions is not found")
    })
    @GetMapping()
    public ResponseEntity<List<ReproductionDto>> getAll(@PathVariable Long petId){
        checkPet(petId);
        checkOwnerPet(petId);
        List<Reproduction> reproductions = reproductionService.getAllByPetId(petId);
        return new ResponseEntity<>(reproductionMapper.toDto(reproductions),HttpStatus.OK);
    }

    @Operation(summary = "get reproduction by id")
    @ApiResponse(responseCode = "200", description = "ok",
            content = @Content(schema = @Schema(implementation = ReproductionDto.class)))
    @ApiResponse(responseCode = "400", description = "reproduction not assigned to this pet or pet not yours",
            content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    @ApiResponse(responseCode = "404", description = "reproduction or pet with this id not found",
            content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    @GetMapping("/{reproductionId}")
    public ResponseEntity<ReproductionDto> getById(@PathVariable Long petId,
                                                   @PathVariable Long reproductionId) {
        Reproduction reproduction = reproductionService.getByKey(reproductionId);
        User client = getOptionalOfNullableSecurityUser().orElseThrow(() -> new NotFoundException("client not found"));
        Pet pet = petService.getByKey(petId);

        if (pet == null) {
            log.info("The pet with this id {} was not found", petId);
            throw new NotFoundException(PET_NOT_FOUND);
        }
        if (reproduction == null) {
            log.info("The reproduction with this id {} was not found", reproductionId);
            throw new NotFoundException(REPRODUCTION_NOT_FOUND);
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            log.info("The pet with this id {} is not yours", petId);
            throw new BadRequestException(NOT_YOURS);
        }
        if (!reproduction.getPet().getId().equals(pet.getId())) {
            log.info("The reproduction with this id {}  not assigned to this pet {}", reproduction.getPet().getId(), petId);
            throw new BadRequestException(NOT_ASSIGNED);
        }
        ReproductionDto reproductionDto = reproductionMapper.toDto(reproduction);

        return new ResponseEntity<>(reproductionDto, HttpStatus.OK);
    }


    @Operation(summary = "add new reproduction")
    @ApiResponse(responseCode = "201", description = "reproduction successful created",
            content = @Content(schema = @Schema(implementation = ReproductionDto.class)))
    @ApiResponse(responseCode = "404", description = "pet with this id not found",
            content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    @ApiResponse(responseCode = "400", description = "pet not yours",
            content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    @PostMapping("")
    public ResponseEntity<ReproductionDto> save(@PathVariable Long petId, @Validated(OnCreate.class)
    @RequestBody ReproductionDto reproductionDto) {

        Pet pet = petService.getByKey(petId);
        Reproduction reproduction = reproductionMapper.toEntity(reproductionDto);
        User client = getOptionalOfNullableSecurityUser().orElseThrow(() -> new NotFoundException("client not found"));

        if (pet == null) {
            throw new NotFoundException(PET_NOT_FOUND);
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            throw new BadRequestException(NOT_YOURS);
        }

        reproduction.setId(null);
        reproductionService.persist(reproduction);

        pet.addReproduction(reproduction);
        petService.update(pet);
        log.info("Added reproduction {} to a pet with this id{}", reproductionDto.toString(), petId);
        return new ResponseEntity<>(reproductionMapper.toDto(reproduction), HttpStatus.CREATED);
    }


    @Operation(summary = "update reproduction by id")
    @ApiResponse(responseCode = "200", description = "reproduction successful updated",
            content = @Content(schema = @Schema(implementation = ReproductionDto.class)))
    @ApiResponse(responseCode = "404", description = "reproduction or pet with this id not found",
            content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    @ApiResponse(responseCode = "400", description = "reproduction not assigned to this pet OR \n" +
            "reproductionId in path and in body not equals OR \npet not yours",
            content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    @PutMapping("/{reproductionId}")
    public ResponseEntity<ReproductionDto> update(@PathVariable Long petId,
                                                  @PathVariable Long reproductionId,
                                                  @JsonView(View.Put.class)
                                                  @RequestBody ReproductionDto reproductionDto) {

        Pet pet = petService.getByKey(petId);
        Reproduction reproduction = reproductionService.getByKey(reproductionId);
        User client = getOptionalOfNullableSecurityUser().orElseThrow(() -> new NotFoundException("client not found"));

        if (pet == null) {
            throw new NotFoundException(PET_NOT_FOUND);
        }
        if (reproduction == null) {
            throw new NotFoundException(REPRODUCTION_NOT_FOUND);
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            throw new BadRequestException(NOT_YOURS);
        }
        if (!reproduction.getPet().getId().equals(pet.getId())) {
            throw new BadRequestException(NOT_ASSIGNED);
        }

        reproductionMapper.updateEntity(reproductionDto, reproduction);
        reproduction.setPet(pet);
        reproduction.setId(reproductionId);
        reproductionService.update(reproduction);
        log.info("Updated reproduction with id {} for a pet with this id{}", reproductionId, petId);

        return new ResponseEntity<>(reproductionMapper.toDto(reproduction), HttpStatus.OK);

    }


    @Operation(summary = "delete reproduction by id")
    @ApiResponse(responseCode = "200", description = "reproduction successful deleted")
    @ApiResponse(responseCode = "404", description = "reproduction or pet with this id not found",
            content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    @ApiResponse(responseCode = "400", description = "reproduction not assigned to this pet OR pet not yours",
            content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    @DeleteMapping(value = "/{reproductionId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long petId, @PathVariable Long reproductionId) {
        Pet pet = petService.getByKey(petId);
        Reproduction reproduction = reproductionService.getByKey(reproductionId);
        User client = getOptionalOfNullableSecurityUser().orElseThrow(() -> new NotFoundException("client not found"));


        if (pet == null) {
            throw new NotFoundException(PET_NOT_FOUND);
        }
        if (reproduction == null) {
            throw new NotFoundException(REPRODUCTION_NOT_FOUND);
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            throw new BadRequestException(NOT_YOURS);
        }
        if (!reproduction.getPet().getId().equals(pet.getId())) {
            throw new BadRequestException(NOT_ASSIGNED);
        }
        pet.removeReproduction(reproduction);
        petService.update(pet);
        log.info("Deleted reproduction with id{} for a pet with this id {}", reproductionId, petId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void checkPet(Long petId){
        if (!petService.isExistByKey(petId)){
            log.info("The pet with this id {} was not found", petId);
            throw new NotFoundException(PET_NOT_FOUND);
        }
    }

    private void checkOwnerPet(Long petId){
        getOptionalOfNullableSecurityUser()
                .filter(user -> petService.isExistByPetIdAndClientId(petId, user.getId()))
                .orElseThrow(() -> {
                    log.info("The pet with this id {} is not yours", petId);
                    throw new BadRequestException(NOT_YOURS);
                });
    }
}