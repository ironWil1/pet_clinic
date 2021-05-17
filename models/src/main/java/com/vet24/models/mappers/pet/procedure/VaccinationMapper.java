package com.vet24.models.mappers.pet.procedure;

import com.vet24.models.dto.pet.procedure.ProcedureDto;
import com.vet24.models.dto.pet.procedure.VaccinationDto;
import com.vet24.models.pet.procedure.VaccinationProcedure;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VaccinationMapper {

    VaccinationDto vaccinationToVaccinationDto(VaccinationProcedure vaccinationProcedure);

    VaccinationProcedure vaccinationDtoToVaccination(VaccinationDto vaccinationDto);

    VaccinationProcedure procedureDtoToVaccination(ProcedureDto procedureDto);
}
