package com.vet24.models.mappers.user;

import com.vet24.models.dto.user.UserRequestDto;
import com.vet24.models.dto.user.UserResponseDto;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "spring", uses = ProfileMapper.class, imports = RoleNameEnum.class)
public interface AdminUserMapper extends DtoMapper<User, UserRequestDto>, EntityMapper<UserResponseDto, User> {
    @Override
    @Mapping(target = "role", expression = "java(user.getRole().getName())")
    UserResponseDto toDto(User user);

    @Override
    @Mapping(target = "role", expression = "java(user.getRole().getName())")
    List<UserResponseDto> toDto(List<User> user);

    @Override
    @Mapping(target = "role", ignore = true)
    User toEntity(UserRequestDto dto);
}
