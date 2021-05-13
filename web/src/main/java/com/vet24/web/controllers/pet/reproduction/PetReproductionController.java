package com.vet24.web.controllers.pet.reproduction;

import com.vet24.models.dto.pet.reproduction.PetReproductionDto;
import com.vet24.models.mappers.pet.reproduction.PetReproductionMapper;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.reproduction.Reproduction;
import com.vet24.service.pet.PetService;
import com.vet24.service.pet.reproduction.ReproductionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client/pet/{petId}/reproduction")
public class PetReproductionController {

    private final PetService petService;
    private final ReproductionService reproductionService;
    private final PetReproductionMapper reproductionMapper;

    @Autowired
    public PetReproductionController(ReproductionService reproductionService, PetReproductionMapper reproductionMapper, PetService petService) {
        this.reproductionService = reproductionService;
        this.reproductionMapper = reproductionMapper;
        this.petService = petService;
    }


    @Operation(summary = "get reproduction by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = PetReproductionDto.class))),
            @ApiResponse(responseCode = "400", description = "reproduction not assigned to this pet"),
            @ApiResponse(responseCode = "404", description = "reproduction or pet with this id not found"),
    })
    @GetMapping("/{reproductionId}")
    public ResponseEntity<PetReproductionDto> getById(@PathVariable Long petId, @PathVariable Long reproductionId) {
        Pet pet = petService.getByKey(petId);
        Reproduction reproduction = reproductionService.getByKey(reproductionId);
        if (pet == null || reproduction == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (!pet.getReproductions().contains(reproduction)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            PetReproductionDto reproductionDto = reproductionMapper.reproductionToReproductionDto(reproduction);
            return new ResponseEntity<>(reproductionDto, HttpStatus.OK);
        }
    }


    @Operation(summary = "add new reproduction")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "reproduction successful created"),
            @ApiResponse(responseCode = "404", description = "pet with this id not found"),
    })
    @PostMapping("")
    public ResponseEntity<Void> save(@PathVariable Long petId,
                                                   @RequestBody PetReproductionDto reproductionDto) {
        Pet pet = petService.getByKey(petId);
        Reproduction reproduction = reproductionMapper.reproductionDtoToReproduction(reproductionDto);
        if (pet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        pet.addReproduction(reproduction);
        petService.update(pet);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @Operation(summary = "update reproduction by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "reproduction successful updated"),
            @ApiResponse(responseCode = "404", description = "reproduction or pet with this id not found"),
            @ApiResponse(responseCode = "400", description = "reproduction not assigned to this pet"),
    })
    @PutMapping("/{reproductionId}")
    public ResponseEntity<Void> update(@PathVariable Long petId, @PathVariable Long reproductionId,
                                                     @RequestBody PetReproductionDto reproductionDto) {
        Pet pet = petService.getByKey(petId);
        Reproduction reproduction = reproductionService.getByKey(reproductionId);

        if (pet == null || reproduction == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (!pet.getReproductions().contains(reproduction)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            reproduction = reproductionMapper.reproductionDtoToReproduction(reproductionDto);
            reproduction.setPet(pet);
            reproduction.setId(reproductionId);
            reproductionService.update(reproduction);

            return new ResponseEntity<>(HttpStatus.OK);
        }
    }


    @Operation(summary = "delete reproduction by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "reproduction successful deleted"),
            @ApiResponse(responseCode = "404", description = "reproduction or pet with this id not found"),
            @ApiResponse(responseCode = "400", description = "reproduction not assigned to this pet"),
    })
    @DeleteMapping(value = "/{reproductionId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long petId, @PathVariable Long reproductionId) {
        Pet pet = petService.getByKey(petId);
        Reproduction reproduction = reproductionService.getByKey(reproductionId);
        if (pet == null || reproduction == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (!pet.getReproductions().contains(reproduction)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            pet.removeReproduction(reproduction);
            petService.update(pet);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
