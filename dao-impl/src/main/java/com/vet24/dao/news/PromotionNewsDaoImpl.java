package com.vet24.dao.news;

import com.vet24.models.news.PromotionNews;
import com.vet24.dao.ReadWriteDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class PromotionNewsDaoImpl extends ReadWriteDaoImpl<Long, PromotionNews> implements PromotionNewsDao {
}


