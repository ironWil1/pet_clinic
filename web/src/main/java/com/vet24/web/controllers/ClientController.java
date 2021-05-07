package com.vet24.web.controllers;

import com.vet24.models.dtos.*;
import com.vet24.models.mappers.MapStructMapper;
import com.vet24.models.pet.Dog;
import com.vet24.models.pet.Pet;
import com.vet24.service.pet.PetService;
import com.vet24.service.user.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/client")
public class ClientController {

    private final ClientService clientService;
    private final PetService petService;
    private final MapStructMapper mapStructMapper;

    public ClientController(ClientService clientService, PetService petService, MapStructMapper mapStructMapper) {
        this.clientService = clientService;
        this.petService = petService;
        this.mapStructMapper = mapStructMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable("id") Long id) {
        ClientDto clientDto = mapStructMapper.clientToClientDto(clientService.getClientById(id));
        return clientDto != null ? ResponseEntity.ok(clientDto) : ResponseEntity.notFound().build();
    }

    // POST /api/client/pet/add
    @PostMapping("/{clientId}/pet/add")
    public ResponseEntity<AbstractNewPetDto> persistPet(@PathVariable("clientId") Long clientId,
                                                        @RequestBody AbstractNewPetDto petDto) {
        if (petDto instanceof DogDto) {
            Dog dog = mapStructMapper.DogDtoToDog((DogDto) petDto);
            dog.setClient(clientService.getClientById(clientId));
            petService.save(dog);
            return ResponseEntity.ok(petDto);
        }
        return ResponseEntity.badRequest().build();
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
//    @PutMapping("/{clientId}/pet/{petId}")
//    public ResponseEntity<PetDto> updatePet() {
//
//    }


    // GET /api/client/pet/{petId}/avatar  получение аватара питомца

    // POST /api/client/pet/{petId}/avatar  сохранение новой аватарки питомца

    // GET /api/client/avatar  получение изображения аватарки

    // POST /api/client/avatar сохранение новой аватарки
}
