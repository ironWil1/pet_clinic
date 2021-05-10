package com.vet24.web.controllers.user;

import com.vet24.models.dto.media.UploadedFileDto;
import com.vet24.models.dto.pet.AbstractNewPetDto;
import com.vet24.models.dto.pet.PetDto;
import com.vet24.models.dto.user.ClientDto;
import com.vet24.models.mappers.pet.PetMapper;
import com.vet24.models.mappers.user.UserMapper;
import com.vet24.models.pet.Pet;
import com.vet24.models.user.Client;
import com.vet24.service.media.ResourceService;
import com.vet24.service.media.UploadService;
import com.vet24.service.pet.PetService;
import com.vet24.service.user.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/client")
@Tag(name = "client-controller", description = "operations with Clients and their Pets")
public class ClientController {

    private final ClientService clientService;
    private final PetService petService;
    private final PetMapper petMapper;
    private final UserMapper userMapper;
    private final UploadService uploadService;
    private final ResourceService resourceService;

    public ClientController(ClientService clientService, PetService petService, PetMapper petMapper,
                            UserMapper userMapper, UploadService uploadService, ResourceService resourceService) {
        this.clientService = clientService;
        this.petService = petService;
        this.petMapper = petMapper;
        this.userMapper = userMapper;
        this.uploadService = uploadService;
        this.resourceService = resourceService;
    }

    @Operation(summary = "get a Client by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the Client by id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Client is not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable("id") Long id) {
        ClientDto clientDto = userMapper.clientToClientDto(clientService.getByKey(id));
        return clientDto != null ? ResponseEntity.ok(clientDto) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "get avatar of a Client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the avatar"),
            @ApiResponse(responseCode = "404", description = "Client or avatar is not found")
    })
    @GetMapping("/{id}/avatar")
    public ResponseEntity<byte[]> getClientAvatar(@PathVariable("id") Long id) {
        Client client = clientService.getByKey(id);
        if (client != null) {
            String url = client.getAvatar();
            boolean isValidUrl = (url != null);
            if (isValidUrl) {
                return new ResponseEntity<>(resourceService.loadAsByteArray(url), addContentHeaders(url), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "upload avatar for a Client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully uploaded the avatar",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UploadedFileDto.class))),
            @ApiResponse(responseCode = "404", description = "Client is not found", content = @Content)
    })
    @PostMapping(value = "/{id}/avatar", consumes = {"multipart/form-data"})
    public ResponseEntity<UploadedFileDto> persistClientAvatar(@PathVariable("id") Long id,
                                                               @RequestParam("file") MultipartFile file) throws IOException {
        Client client = clientService.getByKey(id);
        if (client == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            UploadedFileDto uploadedFileDto = uploadService.store(file);
            client.setAvatar(uploadedFileDto.getUrl());
            clientService.update(client);
            return new ResponseEntity<>(uploadedFileDto, HttpStatus.OK);
        }
    }

    @Operation(summary = "add a new Pet", description = "Pet requires an existing client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added a new Pet",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AbstractNewPetDto.class))),
            @ApiResponse(responseCode = "404", description = "Client is not found", content = @Content)
    })
    @PostMapping("/{clientId}/pet/add")
    public ResponseEntity<AbstractNewPetDto> persistPet(@PathVariable("clientId") Long clientId,
                                                        @RequestBody AbstractNewPetDto petDto) {
        Client client = clientService.getByKey(clientId);
        if (client != null) {
            Pet pet = petMapper.AbstractNewPetDtoToPet(petDto);
            pet.setClient(client);
            petService.persist(pet);
            return ResponseEntity.ok(petDto);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "delete a Pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the Pet"),
            @ApiResponse(responseCode = "404", description = "Pet is not found"),
            @ApiResponse(responseCode = "400", description = "Pet owner ID and url client ID do not match")
    })
    @DeleteMapping("/{clientId}/pet/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable("clientId") Long clientId, @PathVariable("petId") Long petId) {
        Pet pet = petService.getByKey(petId);
        if (pet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (pet.getClient().getId().equals(clientId)) {
            petService.delete(pet);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "update a Pet", description = "Pet requires an existing client. Pet ID before and after must match")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the Pet"),
            @ApiResponse(responseCode = "404", description = "Pet or Client is not found"),
            @ApiResponse(responseCode = "400", description = "Pet owner ID and url client ID do not match")
    })
    @PutMapping("/{clientId}/pet/{petId}")
    public ResponseEntity<PetDto> updatePet(@PathVariable("clientId") Long clientId, @PathVariable("petId") Long petId,
                                            @RequestBody AbstractNewPetDto petDto) {
        Client client = clientService.getByKey(clientId);
        Pet pet = petService.getByKey(petId);
        if (client != null && pet != null) {
            if (pet.getClient().getId().equals(clientId)) {
                Pet updatedPet = petMapper.AbstractNewPetDtoToPet(petDto);
                updatedPet.setId(pet.getId());
                updatedPet.setClient(client);
                petService.update(updatedPet);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "get avatar of a Pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the avatar"),
            @ApiResponse(responseCode = "404", description = "Client or Pet or Pet's avatar is not found")
    })
    @GetMapping(value = "/{clientId}/pet/{petId}/avatar")
    public ResponseEntity<byte[]> getPetAvatar(@PathVariable("clientId") Long clientId,
                                               @PathVariable("petId") Long petId) {
        Client client = clientService.getByKey(clientId);
        Pet pet = petService.getByKey(petId);
        if (client != null && pet != null) {
            String url = pet.getAvatar();
            boolean isValidUrl = (url != null);
            if (isValidUrl) {
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
            @ApiResponse(responseCode = "400", description = "Pet owner ID and url client ID do not match")
    })
    @PostMapping(value = "/{clientId}/pet/{petId}/avatar", consumes = {"multipart/form-data"})
    public ResponseEntity<UploadedFileDto> persistPetAvatar(@PathVariable("clientId") Long clientId,
                                                            @PathVariable("petId") Long petId,
                                                            @RequestParam("file") MultipartFile file) throws IOException {
        Client client = clientService.getByKey(clientId);
        Pet pet = petService.getByKey(petId);
        if (client != null && pet != null) {
            if (pet.getClient().getId().equals(clientId)) {
                UploadedFileDto uploadedFileDto = uploadService.store(file);
                pet.setAvatar(uploadedFileDto.getUrl());
                petService.update(pet);
                return new ResponseEntity<>(uploadedFileDto, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private HttpHeaders addContentHeaders(String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", resourceService.getContentTypeByFileName(filename));
        return headers;
    }
}
