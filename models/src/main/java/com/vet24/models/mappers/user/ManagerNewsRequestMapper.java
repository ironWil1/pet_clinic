package com.vet24.models.mappers.user;

import com.vet24.models.dto.user.ManagerNewsRequestDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.news.News;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ManagerNewsRequestMapper extends DtoMapper<News, ManagerNewsRequestDto>, EntityMapper<ManagerNewsRequestDto, News> {
}

