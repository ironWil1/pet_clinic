package com.vet24.models.mappers.news;

import com.vet24.models.dto.news.AbstractNewNewsDto;
import com.vet24.models.enums.NewsType;
import com.vet24.models.exception.NoSuchAbstractEntityDtoException;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.news.News;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class AbstractNewNewsMapper implements DtoMapper<News, AbstractNewNewsDto> {

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
                    .collect(Collectors.toMap(AbstractNewsMapper::getNewsType,
                                                         Function.identity()));
    }

    public News toEntity(AbstractNewNewsDto abstractNewNewsDto) {
        if (mapperMap
                .containsKey(abstractNewNewsDto.getType())) {
            return mapperMap
                    .get(abstractNewNewsDto.getType())
                    .abstractNewNewsDtoToNews(abstractNewNewsDto);
        } else {
            throw new NoSuchAbstractEntityDtoException("Can't find Mapper for " + abstractNewNewsDto);
        }
    }
}
