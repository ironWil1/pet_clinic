package com.vet24.web.controllers.pet;

import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.dto.pet.PetRequestDto;
import com.vet24.models.dto.pet.PetResponseDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.pet.PetMapper;
import com.vet24.models.pet.Pet;
import com.vet24.models.user.User;
import com.vet24.service.pet.PetService;
import com.vet24.service.pet.appearance.BreedService;
import com.vet24.service.pet.appearance.ColorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Optional;
import java.util.stream.Collectors;

import static com.vet24.models.secutity.SecurityUtil.getOptionalOfNullableSecurityUser;


@RestController
@Slf4j
@RequestMapping("api/client/pet")
@Tag(name = "petClient-controller", description = "operations with Pets")
public class PetClientController {

    private final PetService petService;
    private final PetMapper petMapper;
    private final BreedService breedService;
    private final ColorService colorService;


    public PetClientController(PetService petService, PetMapper petMapper,
                               BreedService breedService, ColorService colorService) {
        this.petService = petService;
        this.petMapper = petMapper;
        this.breedService = breedService;
        this.colorService = colorService;
    }

    @Operation(summary = "get pet by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully get a Pet", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PetResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not yours", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class)))})
    @GetMapping("/{petId}")
    public ResponseEntity<PetResponseDto> getById(@PathVariable("petId") Long petId) {
        Pet pet = petService.getByKey(petId);

        if (pet == null) {
            log.info("The pet with this id {} was not found", petId);
            throw new NotFoundException("pet not found");
        }
        getOptionalOfNullableSecurityUser().map(User::getId).filter(userId -> pet.getClient().getId().equals(userId)).orElseThrow(() -> {
            log.info("The pet with this id {} is not yours", petId);
            throw new BadRequestException("pet not yours");
        });


        log.info("We have pet with this id {}", petId);
        return new ResponseEntity<>(petMapper.toDto(pet), HttpStatus.OK);
    }

    @Operation(summary = "get all pets of current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got Pets")
    })
    @GetMapping
    public ResponseEntity<List<PetResponseDto>> getAllPetsOfCurrentUser() {
        Optional<User> client = getOptionalOfNullableSecurityUser();
        List<Pet> petsOfCurrentUser = petService.getAll().stream().filter(pet ->
                pet.getClient().getId().equals(client.get().getId())).collect(Collectors.toList());

        return new ResponseEntity<>(petMapper.toDto(petsOfCurrentUser), HttpStatus.OK);
    }

    @Operation(summary = "add a new Pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added a new Pet", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PetResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Client is not found", content = @Content)})
    @PostMapping
    public ResponseEntity<PetResponseDto> persistPet(@Validated(PetRequestDto.Post.class) @RequestBody PetRequestDto petRequestDto) {
        boolean hasColor = colorService.findColor(petRequestDto.getColor())
                .stream().anyMatch(petRequestDto.getColor()::equalsIgnoreCase);
        boolean hasBreed = breedService.getBreed(petRequestDto.getPetType().name(), petRequestDto.getBreed())
                .stream().anyMatch(petRequestDto.getBreed()::equalsIgnoreCase);

        if (!hasColor) {
            throw new NotFoundException("No such color is presented");
        }

        if (!hasBreed) {
            throw new NotFoundException("No such breed is presented");
        }

        return getOptionalOfNullableSecurityUser().map(user -> {
            Pet pet = petMapper.toEntity(petRequestDto);
            pet.setClient(user);
            petService.persist(pet);
            log.info("We added new pet {}", petRequestDto.getName());
            return ResponseEntity.status(201).body(petMapper.toDto(pet));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "delete a Pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the Pet"),
            @ApiResponse(responseCode = "404", description = "Pet or Client is not found"),
            @ApiResponse(responseCode = "400", description = "Pet owner ID and current Client ID do not match")})
    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable("petId") Long petId) {
        Optional<User> client = getOptionalOfNullableSecurityUser();
        Pet pet = petService.getByKey(petId);
        if (client.isPresent() && pet != null) {
            if (pet.getClient().getId().equals(client.get().getId())) {
                petService.delete(pet);
                log.info("We deleted pet with this id {}", petId);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "update a Pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the Pet"),
            @ApiResponse(responseCode = "404", description = "Pet or Client is not found"),
            @ApiResponse(responseCode = "400", description = "Pet owner ID and current Client ID do not match")})
    @PutMapping("/{petId}")
    public ResponseEntity<PetResponseDto> updatePet(@PathVariable("petId") Long petId, @Validated(PetRequestDto.Put.class) @RequestBody PetRequestDto petRequestDto) {

        if (petRequestDto.getColor() != null) {
            if(colorService.findColor(petRequestDto.getColor())
                    .stream().noneMatch(petRequestDto.getColor()::equalsIgnoreCase)) {
                throw new NotFoundException("No such color is exists");
            }
        }

        if (petRequestDto.getBreed() != null) {
            if(breedService.getBreed(petRequestDto.getPetType().name(), petRequestDto.getBreed())
                    .stream().noneMatch(petRequestDto.getBreed()::equalsIgnoreCase)) {
                throw new NotFoundException("No such breed exists");
            }
        }

        Optional<User> client = getOptionalOfNullableSecurityUser();
        Pet pet = petService.getByKey(petId);
        if (pet == null || client.isEmpty()) {
            throw new NotFoundException("Pet is not found");
        }
        if (!(pet.getPetType().equals(petRequestDto.getPetType())) & petRequestDto.getPetType() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!(pet.getClient().getId().equals(client.get().getId()))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        petMapper.updateEntity(petRequestDto, pet);
        petService.update(pet);
        log.info("We updated pet with this id {}", petId);
        return ResponseEntity.ok().body(petMapper.toDto(pet));
    }
}