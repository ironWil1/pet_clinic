package com.vet24.web.controllers;

import com.vet24.models.dto.media.UploadedFileDto;
import com.vet24.models.dtos.*;
import com.vet24.models.mappers.MapStructMapper;
import com.vet24.models.pet.Pet;
import com.vet24.models.user.Client;
import com.vet24.service.media.ResourceService;
import com.vet24.service.media.UploadService;
import com.vet24.service.pet.PetService;
import com.vet24.service.user.ClientService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("api/client")
public class ClientController {

    private final ClientService clientService;
    private final PetService petService;
    private final MapStructMapper mapStructMapper;
    private final UploadService uploadService;
    private final ResourceService resourceService;

    public ClientController(ClientService clientService, PetService petService, MapStructMapper mapStructMapper,
                            UploadService uploadService, ResourceService resourceService) {
        this.clientService = clientService;
        this.petService = petService;
        this.mapStructMapper = mapStructMapper;
        this.uploadService = uploadService;
        this.resourceService = resourceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable("id") Long id) {
        ClientDto clientDto = mapStructMapper.clientToClientDto(clientService.getByKey(id));
        return clientDto != null ? ResponseEntity.ok(clientDto) : ResponseEntity.notFound().build();
    }

    // GET /api/client/avatar  получение изображения аватарки
    @GetMapping("/{id}/avatar")
    public ResponseEntity<byte[]> getAvatar(@PathVariable("id") Long id) {
        Client client = clientService.getByKey(id);
        if (client == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            String url = client.getAvatar();
            String filename = StringUtils.substringAfterLast(url, File.separator);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", resourceService.getContentTypeByFileName(filename));
            return url != null
                    ? new ResponseEntity<>(resourceService.loadAsByteArray(url), headers, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // POST /api/client/avatar сохранение новой аватарки
    @PostMapping(value = "/{id}/avatar", consumes = {"multipart/form-data"})
    public ResponseEntity<UploadedFileDto> persistAvatar(@PathVariable("id") Long id,
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

    // POST /api/client/pet/add
    @PostMapping("/{clientId}/pet/add")
    public ResponseEntity<AbstractNewPetDto> persistPet(@PathVariable("clientId") Long clientId,
                                                        @RequestBody AbstractNewPetDto petDto) {
        Client client = clientService.getByKey(clientId);
        if (client != null) {
            Pet pet = mapStructMapper.AbstractNewPetDtoToPet(petDto);
            pet.setClient(client);
            petService.save(pet);
            return ResponseEntity.ok(petDto);
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE /api/client/pet/{petId} само собой удалить можно только своего питомца
    @DeleteMapping("/{clientId}/pet/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable("clientId") Long clientId, @PathVariable("petId") Long petId) {
        Pet pet = petService.findById(petId);
        if (pet == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (pet.getClient().getId().equals(clientId)) {
            petService.delete(petId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // PUT /api/client/pet/{petId} изменение данных питомца
    @PutMapping("/{clientId}/pet/{petId}")
    public ResponseEntity<PetDto> updatePet(@PathVariable("clientId") Long clientId, @PathVariable("petId") Long petId,
                                            @RequestBody PetDto petDto) {
        Client client = clientService.getByKey(clientId);
        Pet pet = petService.findById(petId);
        if (client != null && pet != null) {
            // petDto convert to Pet updatedPet(it's abstract, can't)
            // petService.update(updatedPet)
            //
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    // GET /api/client/pet/{petId}/avatar  получение аватара питомца

    // POST /api/client/pet/{petId}/avatar  сохранение новой аватарки питомца
}
