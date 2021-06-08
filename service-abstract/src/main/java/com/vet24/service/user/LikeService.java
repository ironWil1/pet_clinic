package com.vet24.service.user;

import com.vet24.models.user.Like;
import com.vet24.models.user.LikeId;
import com.vet24.service.ReadWriteService;

import java.util.List;

public interface LikeService extends ReadWriteService<LikeId, Like> {

    List<Like> findByClientId(Long clinetId);
}
