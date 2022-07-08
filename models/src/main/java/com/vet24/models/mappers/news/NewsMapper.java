package com.vet24.models.mappers.news;

import com.vet24.models.dto.news.NewsDto;
import com.vet24.models.enums.NewsType;
import com.vet24.models.exception.NoSuchAbstractEntityDtoException;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.news.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public class NewsMapper implements DtoMapper<News, NewsDto>, EntityMapper<NewsDto, News> {

    private Map<NewsType, AbstractNewsMapper> mapperMap;

    @Autowired
    private List<AbstractNewsMapper> mapperList;

    @PostConstruct
    private void init() {
        this.setMapperMap(mapperList);
    }

    private void setMapperMap(List<AbstractNewsMapper> mapperList) {
        mapperMap = mapperList
                .stream()
                .collect(Collectors.toMap(AbstractNewsMapper::getNewsType, Function.identity()));
    }

    @Mapping(target = "type", source = "type")
    @Override
    public NewsDto toDto(News news) {
        NewsDto newsDto = new NewsDto();
        if (news != null) {
            newsDto.setId(news.getId());
            newsDto.setType(news.getType());
            newsDto.setContent(news.getContent());
            newsDto.setImportant(news.isImportant());
            newsDto.setEndTime(news.getEndTime());
        }
        return newsDto;
    }

    @Override
    public List<NewsDto> toDto(List<News> entities) {
        List<NewsDto> listNewsDto = new ArrayList<>();
        if (entities != null) {
            for (News news : entities) {
                NewsDto newsDto = toDto(news);
                listNewsDto.add(newsDto);
            }
        }
        return listNewsDto;
    }

    @Override
    public void updateEntity(NewsDto dto, News entity) {
        if (dto != null) {
            if (entity != null) {
                entity.setId(dto.getId());
                entity.setType(dto.getType());
                entity.setContent(dto.getContent());
                entity.setImportant(dto.isImportant());
                entity.setEndTime(dto.getEndTime());
            }
        }
    }


    @Override
    public News toEntity(NewsDto newsDto) {
        if (mapperMap
                .containsKey(newsDto.getType())) {
            return mapperMap
                    .get(newsDto.getType())
                    .abstractNewsDtoToNews(newsDto);
        } else {
            throw new NoSuchAbstractEntityDtoException("Can't find Mapper for " + newsDto);
        }
    }

    @Override
    public List<News> toEntity(List<NewsDto> dto) {
        List<News> listNews = new ArrayList<>();
        if (dto != null) {
            for (NewsDto newsDto : dto) {
                News news = toEntity(newsDto);
                listNews.add(news);
            }
        }
        return listNews;
    }

}
