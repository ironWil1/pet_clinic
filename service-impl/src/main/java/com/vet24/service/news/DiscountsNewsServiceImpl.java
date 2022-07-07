package com.vet24.service.news;

import com.vet24.models.news.DiscountsNews;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DiscountsNewsServiceImpl extends ReadWriteServiceImpl<Long, DiscountsNews> implements DiscountsNewsService {

    private final DiscountsNewsDao discountsNewsDao;

    @Autowired
    public DiscountsNewsServiceImpl(DiscountsNewsDao discountsNewsDao) {
        super(discountsNewsDao);
        this.discountsNewsDao = discountsNewsDao;
    }
}
