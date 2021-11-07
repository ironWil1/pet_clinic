package com.vet24.models.mappers.user;

import com.vet24.models.dto.user.DoctorScheduleDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.medicine.DoctorSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorScheduleMapper extends DtoMapper<DoctorSchedule, DoctorScheduleDto>, EntityMapper<DoctorScheduleDto, DoctorSchedule> {

    @Override
    @Mapping(source = "doctorId", target = "doctor.id")
    DoctorSchedule toEntity(DoctorScheduleDto dto);

    @Override
    @Mapping(source = "doctor.id", target = "doctorId")
    DoctorScheduleDto toDto(DoctorSchedule entity);
}
