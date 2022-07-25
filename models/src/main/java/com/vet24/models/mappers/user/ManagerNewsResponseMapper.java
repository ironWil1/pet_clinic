package com.vet24.models.mappers.user;

import com.vet24.models.dto.user.ManagerNewsResponseDto;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.news.News;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ManagerNewsResponseMapper extends EntityMapper<ManagerNewsResponseDto, News> {
}
