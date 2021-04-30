package com.vet24.models.mappers;

import com.vet24.models.dtos.ClientDto;
import com.vet24.models.user.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    @Mapping(source = "login", target = "username")
    ClientDto clientToClientDto(Client client);
}
