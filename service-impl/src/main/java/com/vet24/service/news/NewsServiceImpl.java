package com.vet24.service.news;

import com.vet24.dao.news.NewsDao;
import com.vet24.models.dto.user.ClientNewsResponseDto;
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

    @Override
    public List<ClientNewsResponseDto> getClientNewsResponseDto() {
        return newsDao.getClientNewsResponseDto();
    }

    @Transactional
    @Override
    public Map<Long, String> unpublishNews(List<Long> ids) {
        Map<Long, String> notUnpublishNews = new HashMap<>();
        List<News> news = newsDao.getNewsById(ids);

        List<Long> newsId = news.stream()
                .map(News::getId)
                .collect(Collectors.toList());

        Set<Long> nonExistentNewsIds = ids.stream()
                .filter(i -> !newsId.contains(i))
                .collect(Collectors.toSet());

        List<Long> pastNewsIds = news.stream()
                .filter(pastNews -> pastNews.getEndTime().isBefore(LocalDateTime.now()))
                .map(News::getId)
                .collect(Collectors.toList());

        List<Long> unpublishNewsIds = news.stream()
                .filter(pastNews -> !pastNews.getEndTime().isBefore(LocalDateTime.now())
                        && !nonExistentNewsIds.contains(pastNews.getId()))
                .map(News::getId)
                .collect(Collectors.toList());

        newsDao.unpublishNews(unpublishNewsIds);

        for (long id : nonExistentNewsIds) {
            notUnpublishNews.put(id, "новость не существует");
        }

        for (long id : pastNewsIds) {
            notUnpublishNews.put(id, "endData новости уже прошла");
        }

        return notUnpublishNews;
    }
}
