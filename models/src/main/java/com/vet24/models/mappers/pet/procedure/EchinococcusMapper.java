package com.vet24.models.mappers.pet.procedure;

import com.vet24.models.dto.pet.procedure.EchinococcusDto;
import com.vet24.models.dto.pet.procedure.ProcedureDto;
import com.vet24.models.dto.pet.procedure.VaccinationDto;
import com.vet24.models.pet.procedure.EchinococcusProcedure;
import com.vet24.models.pet.procedure.VaccinationProcedure;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EchinococcusMapper {

    EchinococcusDto echinococcusToEchinococcusDto(EchinococcusProcedure echinococcusProcedure);

    EchinococcusProcedure echinococcusDtoToEchinococcus(EchinococcusDto echinococcusDto);

    EchinococcusProcedure procedureDtoToEchinococcus(ProcedureDto procedureDto);
}
