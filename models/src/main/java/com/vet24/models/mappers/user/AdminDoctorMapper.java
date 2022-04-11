package com.vet24.models.mappers.user;

import com.vet24.models.dto.user.DoctorDto;
import com.vet24.models.dto.user.DoctorDtoPost;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.user.Doctor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface AdminDoctorMapper extends DtoMapper<Doctor, DoctorDtoPost>, EntityMapper<DoctorDto, Doctor> {
}
