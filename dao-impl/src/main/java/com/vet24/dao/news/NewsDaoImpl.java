package com.vet24.dao.news;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.dto.user.ClientNewsResponseDto;
import com.vet24.models.news.News;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class NewsDaoImpl extends ReadWriteDaoImpl<Long, News> implements NewsDao {
    @Override
    public List<ClientNewsResponseDto> getClientNewsResponseDto() {
        List<ClientNewsResponseDto> clientNewsResponseDto = manager.createQuery(
                        "select new com.vet24.models.dto.user.ClientNewsResponseDto" +
                                "(n.id," +
                                "n.title," +
                                "n.type," +
                                "n.content)" +
                                " from News n where (n.published = true) and n.endTime > :localDateTime", ClientNewsResponseDto.class
                )
                .setParameter("localDateTime", LocalDateTime.now())
                .getResultList();
        return clientNewsResponseDto;
    }

    public List<News> getNewsById(List<Long> ids) {
        List<News> news = manager.createQuery("select n from News n where n.id in :ids")
                .setParameter("ids", ids)
                .getResultList();
        return news;
    }

    @Override
    public void publishNews(List<Long> ids) {
        manager.createQuery("update News set published= :published where id in :ids")
                .setParameter("published", true)
                .setParameter("ids", ids)
                .executeUpdate();
    }
    @Override
    public void unpublishNews(List<Long> ids) {
        manager.createQuery("update News set published= :published where id in :ids")
                .setParameter("published", false)
                .setParameter("ids", ids)
                .executeUpdate();
    }
}
