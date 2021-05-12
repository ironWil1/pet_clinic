package com.vet24.web.controllers.pet.reproduction;

import com.vet24.models.dto.pet.reproduction.PetReproductionDto;
import com.vet24.models.mappers.pet.reproduction.PetReproductionMapper;
import com.vet24.models.pet.reproduction.Reproduction;
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
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/client/pet/{petId}/reproduction")
public class PetReproductionController {

    private final ReproductionService reproductionService;
    private final PetReproductionMapper reproductionMapper;

    @Autowired
    public PetReproductionController(ReproductionService reproductionService, PetReproductionMapper reproductionMapper) {
        this.reproductionService = reproductionService;
        this.reproductionMapper = reproductionMapper;
    }

    @Operation(summary = "get reproduction by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = PetReproductionDto.class))),
            @ApiResponse(responseCode = "404", description = "reproduction with this id not found"),
    })
    @GetMapping("/{reproductionId}")
    public ResponseEntity<PetReproductionDto> getById(@PathVariable Long petId, @PathVariable Long reproductionId) {
        Reproduction reproduction = reproductionService.getByKey(reproductionId);
        if (reproduction == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            PetReproductionDto reproductionDto = reproductionMapper.reproductionToReproductionDto(reproduction);
            return new ResponseEntity<>(reproductionDto, HttpStatus.OK);
        }
    }

    @Operation(summary = "add new reproduction")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "reproduction successful created"),
    })
    @PostMapping("")
    public ResponseEntity<PetReproductionDto> save(@PathVariable Long petId,
                                                   @RequestBody PetReproductionDto reproductionDto) {
        Reproduction reproduction = reproductionMapper.reproductionDtoToReproduction(reproductionDto);
        reproduction.setId(null);
        reproductionService.persist(reproduction);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "update reproduction by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "reproduction successful updated"),
            @ApiResponse(responseCode = "400", description = "reproduction with this id not found"),
    })
    @PutMapping("/{reproductionId}")
    public ResponseEntity<PetReproductionDto> update(@PathVariable Long petId, @PathVariable Long reproductionId,
                                                     @RequestBody PetReproductionDto reproductionDto) {
        Reproduction reproduction = reproductionService.getByKey(reproductionId);
        if (reproduction == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            reproduction = reproductionMapper.reproductionDtoToReproduction(reproductionDto);
            reproductionService.update(reproduction);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Operation(summary = "delete reproduction by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "reproduction successful deleted"),
            @ApiResponse(responseCode = "400", description = "reproduction with this id not found"),
    })
    @DeleteMapping(value = "/{reproductionId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long petId, @PathVariable Long reproductionId) {
        Reproduction reproduction = reproductionService.getByKey(reproductionId);
        if (reproduction == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            reproductionService.delete(reproduction);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
