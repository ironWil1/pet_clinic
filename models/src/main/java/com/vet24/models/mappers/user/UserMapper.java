package com.vet24.models.mappers.user;

import com.vet24.models.dto.user.ClientDto;
import com.vet24.models.mappers.pet.PetMapper;
import com.vet24.models.user.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {PetMapper.class})
@Component
public interface UserMapper {

    @Mapping(source = "login", target = "username")
    ClientDto clientToClientDto(Client client);
}
