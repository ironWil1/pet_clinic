package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.Topic;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class TopicDaoImpl extends ReadWriteDaoImpl<Long, Topic> implements TopicDao {

    @Override
    public List<Topic> getTopicByClientId(Long id) {
        return manager.createQuery("SELECT topic FROM Topic topic WHERE topic.topicStarter.id = :id", Topic.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public Topic getByTitleAndClientId(String title, Long clientId) {
        try {
            return manager.createQuery("SELECT topic FROM Topic topic where topic.title = :title AND topic.topicStarter.id = :clientId", Topic.class)
                    .setParameter("title", title)
                    .setParameter("clientId", clientId)
                    .getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }
}
