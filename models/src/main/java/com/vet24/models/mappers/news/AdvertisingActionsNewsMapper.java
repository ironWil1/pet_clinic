package com.vet24.models.mappers.news;

import com.vet24.models.dto.news.AbstractNewNewsDto;
import com.vet24.models.dto.news.AdvertisingActionsNewsDto;
import com.vet24.models.dto.news.NewsDto;
import com.vet24.models.enums.NewsType;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.news.AdvertisingActionsNews;
import com.vet24.models.news.News;
import com.vet24.models.news.PromotionNews;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdvertisingActionsNewsMapper extends AbstractNewsMapper,
                                                      DtoMapper<AdvertisingActionsNews,
                                                                AdvertisingActionsNewsDto>,
                                                      EntityMapper<AdvertisingActionsNewsDto,
                                                                   AdvertisingActionsNews> {

    @Override
    default NewsType getNewsType() {
        return NewsType.ADVERTISING_ACTIONS;
    }

    @Override
    default News abstractNewNewsDtoToNews(AbstractNewNewsDto newsDto) {
        return toEntity((AdvertisingActionsNewsDto) newsDto);
    }

    @Override
    default News abstractNewsDtoToNews(NewsDto newsDto) {
        return newsDtoToNews(newsDto);
    }

    PromotionNews newsDtoToNews(NewsDto newsDto);
}
