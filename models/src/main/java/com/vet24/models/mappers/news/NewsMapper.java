package com.vet24.models.mappers.news;

import com.vet24.models.dto.news.NewsDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.news.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public class NewsMapper implements DtoMapper<News, NewsDto>, EntityMapper<NewsDto, News> {

    @Mapping(target = "type", source = "type")
    @Override
    public NewsDto toDto(News news) {
        return AbstractNewsMapper.INSTANCE.toDto(news);
    }

    @Override
    public List<NewsDto> toDto(List<News> entities) {
        return AbstractNewsMapper.INSTANCE.toDto(entities);
    }

    @Override
    public void updateEntity(NewsDto dto, News entity) {
        AbstractNewsMapper.INSTANCE.updateEntity(dto, entity);
    }

    @Override
    public News toEntity(NewsDto dto) {
        return AbstractNewsMapper.INSTANCE.toEntity(dto);
    }

    @Override
    public List<News> toEntity(List<NewsDto> dto) {
        return AbstractNewsMapper.INSTANCE.toEntity(dto);
    }

}
