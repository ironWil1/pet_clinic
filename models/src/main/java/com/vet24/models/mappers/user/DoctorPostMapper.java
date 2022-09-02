package com.vet24.models.mappers.user;

import com.vet24.models.dto.user.DoctorDtoPost;
import com.vet24.models.dto.user.UserDoctorDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.user.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface DoctorPostMapper extends DtoMapper<User, DoctorDtoPost>, EntityMapper<UserDoctorDto, User> {
}
