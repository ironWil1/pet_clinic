package com.vet24.service.news;

import com.vet24.models.dto.user.ClientNewsResponseDto;
import com.vet24.models.news.News;
import com.vet24.service.ReadWriteService;

import java.util.List;
import java.util.Map;


public interface NewsService extends ReadWriteService<Long, News> {
    List<ClientNewsResponseDto> getClientNewsResponseDto();

    Map<Long, String> publishNews(List<Long> ids);
    Map<Long, String> unpublishNews(List<Long> ids);
    void addNewsPicturesById(Long id, List<String> pictures);
}
