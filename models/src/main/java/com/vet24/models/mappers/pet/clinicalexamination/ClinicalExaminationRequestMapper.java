package com.vet24.models.mappers.pet.clinicalexamination;

import com.vet24.models.dto.pet.clinicalexamination.ClinicalExaminationRequestDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.pet.clinicalexamination.ClinicalExamination;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClinicalExaminationRequestMapper extends DtoMapper<ClinicalExamination, ClinicalExaminationRequestDto>,
        EntityMapper<ClinicalExaminationRequestDto, ClinicalExamination> {
}
