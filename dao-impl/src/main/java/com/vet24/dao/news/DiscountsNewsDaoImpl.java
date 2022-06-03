package com.vet24.dao.news;

import com.vet24.models.news.DiscountsNews;
import com.vet24.dao.ReadWriteDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class DiscountsNewsDaoImpl extends ReadWriteDaoImpl<Long, DiscountsNews> implements DiscountsNewsDao {
}
