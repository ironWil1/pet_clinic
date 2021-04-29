package com.vet24.models.utils;

import com.vet24.models.dtos.ClientDto;
import com.vet24.models.dtos.PetDto;
import com.vet24.models.pet.Pet;
import com.vet24.models.user.Client;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class MappingUtils {

    public ClientDto mapToClientDto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setUsername(client.getUsername());
        dto.setAvatar(client.getAvatar());
        dto.setEmail(client.getEmail());
        dto.setPets(client.getPets().stream().map(this::mapToPetDto).collect(Collectors.toList()));
        return dto;
    }

    public Client mapToClient(ClientDto dto) {
        Client client = new Client();
        client.setUsername(dto.getUsername());
        client.setAvatar(dto.getAvatar());
        client.setEmail(dto.getEmail());
        client.setPets(dto.getPets().stream().map(this::mapToPet).collect(Collectors.toList()));
        return client;
    }

    public PetDto mapToPetDto(Pet pet) {
        PetDto dto = new PetDto();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setAvatar(pet.getAvatar());
        dto.setBirthDay(pet.getBirthDay());
        dto.setType(pet.getType());
        dto.setNotificationCount(pet.getNotificationCount());
        return dto;
    }

    public Pet mapToPet(PetDto dto) {
        Pet pet = new Pet();
        pet.setId(dto.getId());
        pet.setName(dto.getName());
        pet.setAvatar(dto.getAvatar());
        pet.setBirthDay(dto.getBirthDay());
        pet.setType(dto.getType());
        pet.setNotificationCount(dto.getNotificationCount());
        return pet;
    }
}
