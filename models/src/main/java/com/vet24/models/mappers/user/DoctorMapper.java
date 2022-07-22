package com.vet24.models.mappers.user;


import com.vet24.models.dto.user.DoctorDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.user.Doctor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",uses = {UserInfoMapper.class})
public interface DoctorMapper extends DtoMapper<Doctor, DoctorDto>, EntityMapper<DoctorDto, Doctor> {

}
