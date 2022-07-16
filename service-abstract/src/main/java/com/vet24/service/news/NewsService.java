package com.vet24.service.news;

import com.vet24.models.dto.user.ClientNewsResponseDto;
import com.vet24.models.news.News;
import com.vet24.service.ReadWriteService;

import java.util.List;

public interface NewsService extends ReadWriteService<Long, News> {
    List<ClientNewsResponseDto> getClientNewsResponseDto();
}
