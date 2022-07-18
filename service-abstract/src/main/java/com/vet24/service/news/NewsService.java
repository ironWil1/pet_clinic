package com.vet24.service.news;

import com.vet24.models.news.News;
import com.vet24.service.ReadWriteService;
import java.util.List;
import java.util.Map;

public interface NewsService extends ReadWriteService<Long, News> {
    Map<Long, String> unpublishNews(List<Long> unpublishNews);
}
