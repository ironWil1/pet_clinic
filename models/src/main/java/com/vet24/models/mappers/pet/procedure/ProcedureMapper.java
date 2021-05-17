package com.vet24.models.mappers.pet.procedure;

import com.vet24.models.dto.pet.PetDto;
import com.vet24.models.dto.pet.procedure.AbstractNewProcedureDto;
import com.vet24.models.dto.pet.procedure.EchinococcusDto;
import com.vet24.models.dto.pet.procedure.ExternalParasiteDto;
import com.vet24.models.dto.pet.procedure.VaccinationDto;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.procedure.Procedure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ProcedureMapper {

    @Autowired
    private VaccinationMapper vaccinationMapper;

    @Autowired
    private ExternalParasiteMapper externalParasiteMapper;

    @Autowired
    private EchinococcusMapper echinococcusMapper;

    @Mapping(source = "petType", target = "type")
    public abstract PetDto petToPetDto(Pet pet);

    public Procedure abstractNewProcedureDtoToProcedure(AbstractNewProcedureDto procedureDto) {
        Procedure procedure = null;
        String procedureType = procedureDto.getType().name();
        switch (procedureType) {
            case "VACCINATION":
                procedure = vaccinationMapper.vaccinationDtoToVaccination((VaccinationDto) procedureDto);
                break;
            case "EXTERNAL_PARASITE":
                procedure = externalParasiteMapper.externalParasiteDtoToExternalParasite((ExternalParasiteDto) procedureDto);
                break;
            case "ECHINOCOCCUS":
                procedure = echinococcusMapper.echinococcusDtoToEchinococcus((EchinococcusDto) procedureDto);
                break;
            default:
                break;
        }

        return procedure;
    }
}
