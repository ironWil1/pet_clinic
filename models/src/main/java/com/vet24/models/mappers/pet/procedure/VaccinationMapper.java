package com.vet24.models.mappers.pet.procedure;

import com.vet24.models.dto.pet.procedure.ProcedureDto;
import com.vet24.models.dto.pet.procedure.VaccinationDto;
import com.vet24.models.pet.procedure.VaccinationProcedure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VaccinationMapper {

    @Mapping(source = "medicine.id", target = "medicineId")
    VaccinationDto vaccinationToVaccinationDto(VaccinationProcedure vaccinationProcedure);

    @Mapping(source = "medicineId", target = "medicine.id")
    VaccinationProcedure vaccinationDtoToVaccination(VaccinationDto vaccinationDto);

    @Mapping(source = "medicineId", target = "medicine.id")
    VaccinationProcedure procedureDtoToVaccination(ProcedureDto procedureDto);
}
