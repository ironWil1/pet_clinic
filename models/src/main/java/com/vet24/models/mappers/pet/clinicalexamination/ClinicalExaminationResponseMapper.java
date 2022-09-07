package com.vet24.models.mappers.pet.clinicalexamination;

import com.vet24.models.dto.pet.clinicalexamination.ClinicalExaminationResponseDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.pet.clinicalexamination.ClinicalExamination;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClinicalExaminationResponseMapper extends
        DtoMapper<ClinicalExamination, ClinicalExaminationResponseDto>,
        EntityMapper<ClinicalExaminationResponseDto, ClinicalExamination> {

    @Mapping(source = "pet.id", target = "petId")
    @Override
    ClinicalExaminationResponseDto toDto(ClinicalExamination clinicalExamination);
}
