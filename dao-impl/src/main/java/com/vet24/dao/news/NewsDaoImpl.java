package com.vet24.dao.news;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.dto.news.NewsDto;
import com.vet24.models.news.News;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class NewsDaoImpl extends ReadWriteDaoImpl<Long, News> implements NewsDao {
    @Override
    public List<NewsDto> publishNews(List<Long> publishNewsId) {
        manager.createQuery("update News set published= :published where id in :ids and endTime >= :endTime")
                .setParameter("published", true)
                .setParameter("ids", publishNewsId)
                .setParameter("endTime", LocalDateTime.now())
                .executeUpdate();

        List<NewsDto> listNewsId = manager.createQuery("select new com.vet24.models.dto.news.NewsDto" +
                        "(n.id, " +
                        "n.endTime)" +
                        " from News n where n.id in :ids", NewsDto.class)
                .setParameter("ids", publishNewsId)
                .getResultList();
        return listNewsId;
    }
}
