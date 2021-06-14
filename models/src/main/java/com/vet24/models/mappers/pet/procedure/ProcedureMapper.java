package com.vet24.models.mappers.pet.procedure;

import com.vet24.models.dto.pet.procedure.EchinococcusDto;
import com.vet24.models.dto.pet.procedure.ExternalParasiteDto;
import com.vet24.models.dto.pet.procedure.ProcedureDto;
import com.vet24.models.dto.pet.procedure.VaccinationDto;
import com.vet24.models.exception.NoSuchAbstractEntityDtoException;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.pet.procedure.EchinococcusProcedure;
import com.vet24.models.pet.procedure.ExternalParasiteProcedure;
import com.vet24.models.pet.procedure.Procedure;
import com.vet24.models.pet.procedure.VaccinationProcedure;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ProcedureMapper implements DtoMapper<Procedure, ProcedureDto>,
        EntityMapper<ProcedureDto, Procedure> {

    @Autowired
    ExternalParasiteMapper externalParasiteMapper;
    @Autowired
    EchinococcusMapper echinococcusMapper;
    @Autowired
    VaccinationMapper vaccinationMapper;

    @Override
    public Procedure toEntity (ProcedureDto dto){
        if (dto instanceof EchinococcusDto) {
            return echinococcusMapper.toEntity((EchinococcusDto) dto);
        }
        if (dto instanceof ExternalParasiteDto) {
            return externalParasiteMapper.toEntity((ExternalParasiteDto) dto);
        }
        if (dto instanceof VaccinationDto) {
            return vaccinationMapper.toEntity((VaccinationDto) dto);
        }
        throw new NoSuchAbstractEntityDtoException("Can't find mapper for ProcedureDto: " + dto);
    }

    @Override
    public ProcedureDto toDto (Procedure entity){
        if (entity instanceof EchinococcusProcedure) {
            return echinococcusMapper.toDto((EchinococcusProcedure) entity);
        }
        if (entity instanceof ExternalParasiteProcedure) {
            return externalParasiteMapper.toDto((ExternalParasiteProcedure) entity);
        }
        if (entity instanceof VaccinationProcedure) {
            return vaccinationMapper.toDto((VaccinationProcedure) entity);
        }
        throw new NoSuchAbstractEntityDtoException("Can't find mapper for Procedure: " + entity);
    }

}
