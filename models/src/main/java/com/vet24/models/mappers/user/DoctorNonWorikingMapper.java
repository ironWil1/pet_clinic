package com.vet24.models.mappers.user;

import com.vet24.models.dto.user.DoctorDto;
import com.vet24.models.dto.user.DoctorNonWorkingDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.user.Doctor;
import com.vet24.models.user.DoctorNonWorking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorNonWorikingMapper extends DtoMapper<DoctorNonWorking, DoctorNonWorkingDto>, EntityMapper<DoctorNonWorkingDto,DoctorNonWorking> {
    @Override
    @Mapping(source = "doctor",target = "doctorDto")
    DoctorNonWorkingDto toDto(DoctorNonWorking entity);

    @Override
    @Mapping(source = "doctorDto", target = "doctor")
    DoctorNonWorking toEntity(DoctorNonWorkingDto dto);



}
