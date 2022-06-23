package com.vet24.models.mappers.news;


import com.vet24.models.dto.news.NewsDto;
import com.vet24.models.enums.NewsType;
import com.vet24.models.news.News;

public interface AbstractNewsMapper {
    NewsType getNewsType();

    News abstractNewsDtoToNews(NewsDto newsDto);
}
