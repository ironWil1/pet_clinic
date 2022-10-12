package com.vet24.service.news;

import com.vet24.dao.news.NewsDao;
import com.vet24.models.discord.DiscordMessage;
import com.vet24.models.dto.user.ClientNewsResponseDto;
import com.vet24.models.mappers.news.NewsMessageDTOMapper;
import com.vet24.models.news.News;
import com.vet24.service.ReadWriteServiceImpl;
import com.vet24.service.discord.DiscordService;
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

    private final DiscordService discordService;

    private final NewsMessageDTOMapper newsMessageDTOMapper;

    @Autowired
    public NewsServiceImpl(NewsDao newsDao, DiscordService discordService, NewsMessageDTOMapper newsMessageDTOMapper) {
        super(newsDao);
        this.newsDao = newsDao;
        this.discordService = discordService;
        this.newsMessageDTOMapper = newsMessageDTOMapper;
    }

    @Override
    public List<ClientNewsResponseDto> getClientNewsResponseDto() {
        return newsDao.getClientNewsResponseDto();
    }

    @Transactional
    @Override
    public Map<Long, String> publishNews(List<Long> ids) {
        Map<Long, String> notPublishNews = new HashMap<>();
        List<News> news = newsDao.getNewsById(ids);

        List<Long> newsIds = news.stream()
                .map(News::getId)
                .collect(Collectors.toList());

        Set<Long> nonExistentNewsIds = ids.stream()
                .filter(i -> !newsIds.contains(i))
                .collect(Collectors.toSet());

        List<Long> pastNewsIds = news.stream()
                .filter(newsDto -> newsDto.getEndTime().isBefore(LocalDateTime.now()))
                .map(News::getId)
                .collect(Collectors.toList());

        List<Long> publishNewsIds = news.stream()
                .filter(pastNews -> !pastNews.getEndTime().isBefore(LocalDateTime.now())
                        && !nonExistentNewsIds.contains(pastNews.getId()))
                .map(News::getId)
                .collect(Collectors.toList());

        newsDao.publishNews(publishNewsIds);

        for (News publishedNews: newsDao.getNewsById(publishNewsIds)) {
            System.out.println(newsMessageDTOMapper.toDto(publishedNews));
            DiscordMessage discordMessage = discordService
                    .sendMessage(newsMessageDTOMapper.toDto(publishedNews));
            publishedNews.setDiscordMessage(discordMessage);
            newsDao.update(publishedNews);
        }

        for (long id : nonExistentNewsIds) {
            notPublishNews.put(id, "новость не существует");
        }

        for (long id : pastNewsIds) {
            notPublishNews.put(id, "endData новости уже прошла");
        }

        return notPublishNews;
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

        for (News unpublishedNews: newsDao.getNewsById(unpublishNewsIds)) {
            if(unpublishedNews.getDiscordMessage() != null) {
                discordService.deleteMessage(
                        unpublishedNews.getDiscordMessage()
                                .getDiscordMsgId());
            }
            unpublishedNews.setDiscordMessage(null);
        }

        newsDao.unpublishNews(unpublishNewsIds);

        for (long id : nonExistentNewsIds) {
            notUnpublishNews.put(id, "новость не существует");
        }

        for (long id : pastNewsIds) {
            notUnpublishNews.put(id, "endData новости уже прошла");
        }

        return notUnpublishNews;
    }

    @Transactional
    @Override
    public void addNewsPicturesById(Long id, List<String> pictures) {
        News news = newsDao.getByKey(id);
        if (news != null) {
            news.setPictures(pictures);
            newsDao.update(news);
        }
    }

    @Transactional
    @Override
    public News update(News news) {
        if (news.getDiscordMessage() != null) {
            discordService.editMessage(news.getDiscordMessage().getDiscordMsgId(),
                    newsMessageDTOMapper.toDto(news));
        }
        return super.update(news);
    }

    @Transactional
    @Override
    public void delete(News news) {
        if (news.getDiscordMessage() != null) {
            discordService.deleteMessage(news.getDiscordMessage().getDiscordMsgId());
        }
        super.delete(news);
    }
}
