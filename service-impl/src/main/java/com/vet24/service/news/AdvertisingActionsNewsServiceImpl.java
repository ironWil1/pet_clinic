package com.vet24.service.news;

import com.vet24.models.news.AdvertisingActionsNews;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AdvertisingActionsNewsServiceImpl extends ReadWriteServiceImpl<Long, AdvertisingActionsNews> implements AdvertisingActionsNewsService {

    private final AdvertisingActionsNewsDao advertisingActionsNewsDao;

    @Autowired
    public AdvertisingActionsNewsServiceImpl(AdvertisingActionsNewsDao advertisingActionsNewsDao) {
        super(advertisingActionsNewsDao);
        this.advertisingActionsNewsDao = advertisingActionsNewsDao;
    }
}
