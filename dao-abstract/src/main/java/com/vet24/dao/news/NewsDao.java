package com.vet24.dao.news;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.dto.news.NewsDto;
import com.vet24.models.news.News;

import java.util.List;

public interface NewsDao extends ReadWriteDao<Long, News> {
    List<NewsDto> unpublishNews(List<Long> unpublishNews);
}
