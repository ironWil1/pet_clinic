package com.vet24.service.news;

import com.vet24.dao.news.NewsDao;
import com.vet24.models.dto.user.ClientNewsResponseDto;
import com.vet24.models.news.News;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
