package com.vet24.models.mappers.user;

import com.vet24.models.dto.user.UserInfoDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserInfoMapper extends DtoMapper<User, UserInfoDto>, EntityMapper<UserInfoDto, User> {


}
