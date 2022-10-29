package com.vet24.models.mappers.user;

import com.vet24.models.dto.user.DoctorDtoPost;
import com.vet24.models.dto.user.UserDoctorDto;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.user.Role;
import com.vet24.models.user.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface DoctorPostMapper extends DtoMapper<User, DoctorDtoPost>, EntityMapper<UserDoctorDto, User> {
    @Override
    default User toEntity(DoctorDtoPost dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();

        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(new Role(RoleNameEnum.DOCTOR));

        return user;
    }
}
