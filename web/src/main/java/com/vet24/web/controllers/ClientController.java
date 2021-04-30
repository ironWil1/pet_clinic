package com.vet24.web.controllers;

import com.vet24.models.dtos.ClientDto;
import com.vet24.models.mappers.ClientMapper;
import com.vet24.service.user.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/client")
public class ClientController {

    private final ClientService clientService;
    private final ClientMapper clientMapper;

    public ClientController(ClientService clientService, ClientMapper clientMapper) {
        this.clientService = clientService;
        this.clientMapper = clientMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClient(@PathVariable("id") Long id) {
        ClientDto clientDto = clientMapper.clientToClientDto(clientService.getClientById(id));
        return clientDto != null ? ResponseEntity.ok(clientDto) : ResponseEntity.notFound().build();
    }

    // POST /api/client/pet/add

    // DELETE /api/client/pet/{petId} само собой удалить можно только своего питомца

    // PUT /api/client/pet/{petId} изменение данных питомца

    // GET /api/client/pet/{petId}/avatar  получение аватара питомца

    // POST /api/client/pet/{petId}/avatar  сохранение новой аватарки питомца

    // GET /api/client/avatar  получение изображения аватарки

    // POST /api/client/avatar сохранение новой аватарки
}
