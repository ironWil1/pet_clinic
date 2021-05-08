package com.vet24.models.mappers.user;

import com.vet24.models.dto.user.ClientDto;
import com.vet24.models.user.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "login", target = "username")
    ClientDto clientToClientDto(Client client);
}
