package com.vet24.service.news;

import com.vet24.models.news.UpdatingNews;
import com.vet24.dao.news.UpdatingNewsDao;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class UpdatingNewsServiceImpl extends ReadWriteServiceImpl<Long, UpdatingNews> implements UpdatingNewsService {

    private final UpdatingNewsDao updatingNewsDao;

    @Autowired
    public UpdatingNewsServiceImpl(UpdatingNewsDao updatingNewsDao) {
        super(updatingNewsDao);
        this.updatingNewsDao = updatingNewsDao;
    }
}

