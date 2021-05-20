package com.vet24.models.mappers.user;


import com.vet24.models.dto.user.ClientDto;
import com.vet24.models.dto.user.RegisterDto;
import com.vet24.models.mappers.pet.PetMapper;
import com.vet24.models.user.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PetMapper.class})
public interface ClientMapper {

    @Mapping(source = "email", target = "username")
    ClientDto clientToClientDto(Client client);

    Client registerDtoToClient(RegisterDto dto);
}
