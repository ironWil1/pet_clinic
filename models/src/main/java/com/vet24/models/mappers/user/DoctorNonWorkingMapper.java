package com.vet24.models.mappers.user;

import com.vet24.models.dto.user.DoctorNonWorkingDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.user.DoctorNonWorking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorNonWorkingMapper extends DtoMapper<DoctorNonWorking, DoctorNonWorkingDto>, EntityMapper<DoctorNonWorkingDto,DoctorNonWorking> {


    @Override
    DoctorNonWorkingDto toDto(DoctorNonWorking entity);

    @Override
    DoctorNonWorking toEntity(DoctorNonWorkingDto dto);



}
