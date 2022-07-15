package com.vet24.models.mappers.user;

import com.vet24.models.dto.user.UserInfoDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserInfoMapper extends DtoMapper<User, UserInfoDto>, EntityMapper<UserInfoDto, User> {

    @Override
    @Mapping(source = "entity.profile.firstName", target = "firstname")
    @Mapping(source = "entity.profile.lastName", target = "lastname")
    UserInfoDto toDto(User entity);

}
