package com.vet24.service.news;

import com.vet24.dao.news.PromotionNewsDao;
import com.vet24.models.news.PromotionNews;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class PromotionNewsServiceImpl extends ReadWriteServiceImpl<Long, PromotionNews> implements PromotionNewsService {

    private final PromotionNewsDao promotionNewsDao;

    @Autowired
    public PromotionNewsServiceImpl (PromotionNewsDao promotionNewsDao) {
        super(promotionNewsDao);
        this.promotionNewsDao = promotionNewsDao;
    }
}

