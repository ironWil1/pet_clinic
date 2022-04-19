package com.vet24.web.controllers.pet;

import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.dto.media.UploadedFileDto;
import com.vet24.models.dto.pet.AbstractNewPetDto;
import com.vet24.models.dto.pet.PetDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.pet.AbstractNewPetMapper;
import com.vet24.models.mappers.pet.PetMapper;
import com.vet24.models.pet.Pet;
import com.vet24.models.user.Client;
import com.vet24.service.media.ResourceService;
import com.vet24.service.media.UploadService;
import com.vet24.service.pet.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import javax.validation.Valid;
import java.io.IOException;

import static com.vet24.models.secutity.SecurityUtil.getPrincipalOrNull;

@RestController
@Slf4j
@RequestMapping("api/client/pet")
@Tag(name = "pet-controller", description = "operations with Pets")
public class PetController {

    private final PetService petService;
    private final PetMapper petMapper;
    private final AbstractNewPetMapper newPetMapper;
    private final UploadService uploadService;
    private final ResourceService resourceService;

    public PetController(PetService petService, PetMapper petMapper, AbstractNewPetMapper newPetMapper,
                         UploadService uploadService, ResourceService resourceService) {
        this.petService = petService;
        this.petMapper = petMapper;
        this.newPetMapper = newPetMapper;
        this.uploadService = uploadService;
        this.resourceService = resourceService;
    }

    @Operation(summary = "get pet by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully get a Pet",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PetDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Pet not yours",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping("/{petId}")
    public ResponseEntity<PetDto> getById(@PathVariable("petId") Long petId) {
//        Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = (Client) getPrincipalOrNull();
        Pet pet = petService.getByKey(petId);

        if (pet == null) {
            log.info("The pet with this id {} was not found", petId);
            throw new NotFoundException("pet not found");
        }
        if (!pet.getClient().getId().equals(client.getId())) {
            log.info("The pet with this id {} is not yours", petId);
            throw new BadRequestException("pet not yours");
        }

        log.info("We have pet with this id {}", petId);
        return new ResponseEntity<>(petMapper.toDto(pet), HttpStatus.OK);
    }

    @Operation(summary = "add a new Pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added a new Pet",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AbstractNewPetDto.class))),
            @ApiResponse(responseCode = "404", description = "Client is not found", content = @Content)
    })
    @PostMapping("/add")
    public ResponseEntity<AbstractNewPetDto> persistPet(@Valid @RequestBody AbstractNewPetDto petDto) {

//        Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = (Client) getPrincipalOrNull();
        if (client != null) {
            Pet pet = newPetMapper.toEntity(petDto);
            pet.setClient(client);
            petService.persist(pet);
            log.info("We added new pet {}", petDto.getName());
            return ResponseEntity.status(201).body(petDto);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "delete a Pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the Pet"),
            @ApiResponse(responseCode = "404", description = "Pet or Client is not found"),
            @ApiResponse(responseCode = "400", description = "Pet owner ID and current Client ID do not match")
    })
    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable("petId") Long petId) {
//        Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = (Client) getPrincipalOrNull();
        Pet pet = petService.getByKey(petId);
        if (client != null && pet != null) {
            if (pet.getClient().getId().equals(client.getId())) {
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
            @ApiResponse(responseCode = "400", description = "Pet owner ID and current Client ID do not match")
    })
    @PutMapping("/{petId}")
    public ResponseEntity<AbstractNewPetDto> updatePet(@PathVariable("petId") Long petId, @Valid
    @RequestBody AbstractNewPetDto petDto) {
//        Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = (Client) getPrincipalOrNull();
        Pet pet = petService.getByKey(petId);
        if (pet == null) {
            throw new NotFoundException("Pet is not found");
        }
        if (!(pet.getPetType().equals(petDto.getPetType()))) {
            if (!(pet.getPetType().equals(petDto.getPetType()))) {
                throw new NotFoundException("The type of pet can not be changed");
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!(pet.getClient().getId().equals(client.getId()))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Pet updatedPet = newPetMapper.toEntity(petDto);
        updatedPet.setId(pet.getId());
        updatedPet.setClient(client);
        petService.update(updatedPet);
        log.info("We updated pet with this id {}", petId);
        return ResponseEntity.ok().body(petDto);
    }


    @Operation(summary = "get avatar of a Pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the avatar"),
            @ApiResponse(responseCode = "404", description = "Client or Pet or Pet's avatar is not found")
    })
    @GetMapping(value = "/{petId}/avatar")
    public ResponseEntity<byte[]> getPetAvatar(@PathVariable("petId") Long petId) {
//        Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = (Client) getPrincipalOrNull();
        Pet pet = petService.getByKey(petId);
        if (client != null && pet != null) {
            String url = pet.getAvatar();
            if (url != null) {
                log.info(" The pet has avatar  {} ", url);
                return new ResponseEntity<>(resourceService.loadAsByteArray(url), addContentHeaders(url), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "upload avatar for a Pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully uploaded the avatar",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UploadedFileDto.class))),
            @ApiResponse(responseCode = "404", description = "Client or Pet is not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Pet owner ID and current Client ID do not match")
    })
    @PostMapping(value = "/{petId}/avatar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UploadedFileDto> persistPetAvatar(@PathVariable("petId") Long petId,
                                                            @RequestParam("file") MultipartFile file) throws IOException {
//        Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = (Client) getPrincipalOrNull();
        Pet pet = petService.getByKey(petId);
        if (client != null && pet != null) {
            if (pet.getClient().getId().equals(client.getId())) {
                UploadedFileDto uploadedFileDto = uploadService.store(file);
                pet.setAvatar(uploadedFileDto.getUrl());
                petService.update(pet);
                log.info(" The pet with this id {} changes avatar  {} ", petId);
                return new ResponseEntity<>(uploadedFileDto, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private HttpHeaders addContentHeaders(String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", resourceService.getContentTypeByFileName(filename));
        return headers;
    }

}
