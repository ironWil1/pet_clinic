package com.vet24.dao.news;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.dto.user.ClientNewsResponseDto;
import com.vet24.models.news.News;
import org.springframework.stereotype.Repository;

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
                        " from News n where n.published = true", ClientNewsResponseDto.class
        ).getResultList();
        return clientNewsResponseDto;
    }
}
