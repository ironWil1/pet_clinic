package com.vet24.web.controllers.pet.reproduction;

import com.vet24.models.dto.pet.reproduction.PetReproductionDto;
import com.vet24.models.mappers.pet.reproduction.PetReproductionMapper;
import com.vet24.models.pet.reproduction.Reproduction;
import com.vet24.service.pet.reproduction.ReproductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 200
    // 404
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

    // 201 created
    @PostMapping("")
    public ResponseEntity<PetReproductionDto> save(@PathVariable Long petId,
                                                   @RequestBody PetReproductionDto reproductionDto) {
        Reproduction reproduction = reproductionMapper.reproductionDtoToReproduction(reproductionDto);
        reproduction.setId(null);
        reproductionService.persist(reproduction);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 200
    // 400
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

    // 400
    // 200
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
