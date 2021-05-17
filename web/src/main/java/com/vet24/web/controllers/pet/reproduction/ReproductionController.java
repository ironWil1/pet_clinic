package com.vet24.web.controllers.pet.reproduction;

import com.vet24.models.dto.pet.reproduction.ReproductionDto;
import com.vet24.models.mappers.pet.reproduction.ReproductionMapper;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.reproduction.Reproduction;
import com.vet24.models.user.Client;
import com.vet24.service.user.ClientService;
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
public class ReproductionController {

    private final PetService petService;
    private final ReproductionService reproductionService;
    private final ReproductionMapper reproductionMapper;
    private final ClientService clientService;

    @Autowired
    public ReproductionController(ReproductionService reproductionService, ReproductionMapper reproductionMapper,
                                  PetService petService, ClientService clientService) {
        this.reproductionService = reproductionService;
        this.reproductionMapper = reproductionMapper;
        this.petService = petService;
        this.clientService = clientService;
    }


    @Operation(summary = "get reproduction by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = ReproductionDto.class))),
            @ApiResponse(responseCode = "400", description = "reproduction not assigned to this pet or pet not yours"),
            @ApiResponse(responseCode = "404", description = "reproduction or pet with this id not found"),
    })
    @GetMapping("/{reproductionId}")
    public ResponseEntity<ReproductionDto> getById(@PathVariable Long petId, @PathVariable Long reproductionId) {
        Pet pet = petService.getByKey(petId);
        Reproduction reproduction = reproductionService.getByKey(reproductionId);
        Client client = clientService.getCurrentClient();

        if (pet == null || reproduction == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (!reproduction.getPet().getId().equals(pet.getId()) || !pet.getClient().getId().equals(client.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            ReproductionDto reproductionDto = reproductionMapper.reproductionToReproductionDto(reproduction);
            return new ResponseEntity<>(reproductionDto, HttpStatus.OK);
        }
    }


    @Operation(summary = "add new reproduction")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "reproduction successful created"),
            @ApiResponse(responseCode = "404", description = "pet with this id not found"),
            @ApiResponse(responseCode = "400", description = "pet not yours"),
    })
    @PostMapping("")
    public ResponseEntity<ReproductionDto> save(@PathVariable Long petId,
                                                @RequestBody ReproductionDto reproductionDto) {
        Pet pet = petService.getByKey(petId);
        Reproduction reproduction = reproductionMapper.reproductionDtoToReproduction(reproductionDto);
        Client client = clientService.getCurrentClient();

        if (pet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (!pet.getClient().getId().equals(client.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        reproduction.setId(null);
        reproductionService.persist(reproduction);

        pet.addReproduction(reproduction);
        petService.update(pet);

        return new ResponseEntity<>(reproductionMapper.reproductionToReproductionDto(reproduction), HttpStatus.CREATED);
    }


    @Operation(summary = "update reproduction by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "reproduction successful updated"),
            @ApiResponse(responseCode = "404", description = "reproduction or pet with this id not found"),
            @ApiResponse(responseCode = "400", description = "reproduction not assigned to this pet OR \n" +
                    "reproductionId in path and in body not equals OR \n" +
                    "pet not yours"),
    })
    @PutMapping("/{reproductionId}")
    public ResponseEntity<ReproductionDto> update(@PathVariable Long petId, @PathVariable Long reproductionId,
                                                  @RequestBody ReproductionDto reproductionDto) {
        Pet pet = petService.getByKey(petId);
        Reproduction reproduction = reproductionService.getByKey(reproductionId);
        Client client = clientService.getCurrentClient();

        if (pet == null || reproduction == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (!reproduction.getPet().getId().equals(pet.getId()) ||
                !reproductionId.equals(reproductionDto.getId()) ||
                !pet.getClient().getId().equals(client.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            reproduction = reproductionMapper.reproductionDtoToReproduction(reproductionDto);
            reproduction.setPet(pet);
            reproductionService.update(reproduction);

            return new ResponseEntity<>(reproductionMapper.reproductionToReproductionDto(reproduction), HttpStatus.OK);
        }
    }


    @Operation(summary = "delete reproduction by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "reproduction successful deleted"),
            @ApiResponse(responseCode = "404", description = "reproduction or pet with this id not found"),
            @ApiResponse(responseCode = "400", description = "reproduction not assigned to this pet OR pet not yours"),
    })
    @DeleteMapping(value = "/{reproductionId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long petId, @PathVariable Long reproductionId) {
        Pet pet = petService.getByKey(petId);
        Reproduction reproduction = reproductionService.getByKey(reproductionId);
        Client client = clientService.getCurrentClient();

        if (pet == null || reproduction == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (!reproduction.getPet().getId().equals(pet.getId()) ||
                !pet.getClient().getId().equals(client.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            pet.removeReproduction(reproduction);
            petService.update(pet);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
