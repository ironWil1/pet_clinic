package com.vet24.models.mappers.news;

import com.vet24.models.dto.news.NewsDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.news.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class NewsMapper implements DtoMapper<News, NewsDto>, EntityMapper<NewsDto, News> {
    @Mapping(target = "type", source = "type")
    @Override
    public abstract NewsDto toDto(News news);
}
