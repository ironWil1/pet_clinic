package com.vet24.models.mappers.user;

import com.vet24.models.dto.user.ManagerNewsRequestDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.news.News;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ManagerNewsRequestMapper extends DtoMapper<News, ManagerNewsRequestDto> {
    void updateEntity(ManagerNewsRequestDto dto, @MappingTarget News entity);
}

