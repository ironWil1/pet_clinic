package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.user.Topic;

import java.util.List;

public interface TopicDao extends ReadWriteDao<Long, Topic> {

    List<Topic> getTopicByClientId(Long id);
}
