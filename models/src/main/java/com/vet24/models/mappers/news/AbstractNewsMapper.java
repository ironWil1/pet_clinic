package com.vet24.models.mappers.news;


import com.vet24.models.dto.news.NewsDto;
import com.vet24.models.news.News;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AbstractNewsMapper {

    AbstractNewsMapper INSTANCE = Mappers.getMapper(AbstractNewsMapper.class);

    NewsDto toDto(News news);

    List<NewsDto> toDto(List<News> entities);

    News toEntity(NewsDto dto);

    List<News> toEntity(List<NewsDto> dto);

    void updateEntity(NewsDto dto, @MappingTarget News entity);
}
