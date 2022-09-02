package com.vet24.models.mappers.user;

import com.vet24.models.dto.user.RegisterDto;
import com.vet24.models.dto.user.UserDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.mappers.pet.PetMapper;
import com.vet24.models.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PetMapper.class,UserInfoMapper.class})
public interface UserMapper extends DtoMapper<User, RegisterDto>, EntityMapper<UserDto, User> {

}
