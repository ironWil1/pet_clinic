package com.vet24.models.mappers.news;

import com.vet24.models.dto.news.DiscountsNewsDto;
import com.vet24.models.dto.news.NewsDto;
import com.vet24.models.enums.NewsType;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.news.DiscountsNews;
import com.vet24.models.news.News;
import com.vet24.models.news.PromotionNews;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiscountsNewsMapper extends AbstractNewsMapper,
                                             DtoMapper<DiscountsNews, DiscountsNewsDto>,
                                             EntityMapper<DiscountsNewsDto, DiscountsNews> {

    @Override
    default NewsType getNewsType() {
        return NewsType.DISCOUNTS;
    }


    @Override
    default News abstractNewsDtoToNews(NewsDto newsDto) {
        return newsDtoToNews(newsDto);
    }

    PromotionNews newsDtoToNews(NewsDto newsDto);

}
