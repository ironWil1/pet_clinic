package com.vet24.models.mappers.news;


import com.vet24.models.dto.news.NewsDto;
import com.vet24.models.dto.news.UpdatingNewsDto;
import com.vet24.models.enums.NewsType;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.news.News;
import com.vet24.models.news.UpdatingNews;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UpdatingNewsMapper extends AbstractNewsMapper,
                                            DtoMapper<UpdatingNews, UpdatingNewsDto>,
                                            EntityMapper<UpdatingNewsDto, UpdatingNews> {

    @Override
    default NewsType getNewsType() {
        return NewsType.UPDATING;
    }

    @Override
    default News abstractNewsDtoToNews(NewsDto newsDto) {
        return newsDtoToNews(newsDto);
    }

    UpdatingNews newsDtoToNews(NewsDto newsDto);

}
