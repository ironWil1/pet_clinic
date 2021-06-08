package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.user.Like;
import com.vet24.models.user.LikeId;

import java.util.List;


public interface LikeDao  extends ReadWriteDao<LikeId, Like> {

    List<Like> findByClientId(Long clinetId);
}
