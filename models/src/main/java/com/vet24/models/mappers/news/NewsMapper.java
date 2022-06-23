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
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class NewsMapper implements
        DtoMapper<News, NewsDto>, EntityMapper<NewsDto, News> {

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
    public abstract NewsDto toDto(News news);


    @Override
    public News toEntity (NewsDto newsDto) {
        if (mapperMap
                .containsKey(newsDto.getType())) {
            return mapperMap
                    .get(newsDto.getType())
                    .abstractNewsDtoToNews(newsDto);
        } else {
            throw new NoSuchAbstractEntityDtoException("Can't find Mapper for " + newsDto);
        }
    }

}
