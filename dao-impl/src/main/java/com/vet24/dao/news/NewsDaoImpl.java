package com.vet24.dao.news;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.news.News;
import org.springframework.stereotype.Repository;

@Repository
public class NewsDaoImpl extends ReadWriteDaoImpl<Long, News> implements NewsDao {
}
