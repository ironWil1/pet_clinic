package com.vet24.web.controllers;

import com.vet24.models.dtos.ClientDto;
import com.vet24.models.user.Client;
import com.vet24.models.utils.MappingUtils;
import com.vet24.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/client")
public class ClientController {

    private final ClientService clientService;
    private final MappingUtils mappingUtils;

    public ClientController(ClientService clientService, MappingUtils mappingUtils) {
        this.clientService = clientService;
        this.mappingUtils = mappingUtils;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClient(@PathVariable("id") Long id) {
        ClientDto clientDto = mappingUtils.mapToClientDto(clientService.getClientById(id));
        return clientDto != null ? ResponseEntity.ok(clientDto) : ResponseEntity.notFound().build();
    }
}
