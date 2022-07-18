package com.vet24.service.news;

import com.vet24.dao.news.NewsDao;
import com.vet24.models.dto.news.NewsDto;
import com.vet24.models.news.News;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl extends ReadWriteServiceImpl<Long, News> implements NewsService {

    private final NewsDao newsDao;

    @Autowired
    public NewsServiceImpl(NewsDao newsDao) {
        super(newsDao);
        this.newsDao = newsDao;
    }

    @Transactional
    @Override
    public Map<Long, String> publishNews(List<Long> publishNewsId) {
        Map<Long, String> notPublishNews = new HashMap<>();
        List<NewsDto> listNewsDto = newsDao.publishNews(publishNewsId);

        List<Long> listNewsId = listNewsDto.stream()
                .map(NewsDto::getId)
                .collect(Collectors.toList());

        Set<Long> notExistNewsId = publishNewsId.stream()
                .filter(i -> !listNewsId.contains(i))
                .collect(Collectors.toSet());

        List<Long> newsIdEndTime = listNewsDto.stream()
                .filter(newsDto -> newsDto.getEndTime().isBefore(LocalDateTime.now()))
                .map(NewsDto::getId)
                .collect(Collectors.toList());

        for (long id : notExistNewsId) {
            notPublishNews.put(id, "новость не существует");
        }

        for (long id : newsIdEndTime) {
            notPublishNews.put(id, "endData новости уже прошла");
        }

        return notPublishNews;
    }
}
