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
    public Topic getTopicWithCommentsById(Long topicId) {
        try {
            return manager
                    .createQuery("SELECT t FROM Topic t LEFT JOIN FETCH t.comments WHERE t.id = :topicId", Topic.class)
                    .setParameter("topicId", topicId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
