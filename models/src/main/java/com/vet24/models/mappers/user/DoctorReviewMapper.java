package com.vet24.models.mappers.user;

import com.vet24.models.dto.user.DoctorReviewDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.user.DoctorReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface DoctorReviewMapper extends DtoMapper<DoctorReview, DoctorReviewDto>,
        EntityMapper<DoctorReviewDto, DoctorReview> {

    @Override
    @Mapping(source = "doctorId", target = "doctor.id")
    @Mapping(source = "review", target = "comment")
    DoctorReview toEntity(DoctorReviewDto dto);

    @Override
    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "comment", target = "review")
    DoctorReviewDto toDto(DoctorReview entity);
}
