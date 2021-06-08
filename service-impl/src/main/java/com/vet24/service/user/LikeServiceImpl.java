package com.vet24.service.user;

import com.vet24.dao.user.LikeDao;
import com.vet24.models.user.Like;
import com.vet24.models.user.LikeId;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl extends ReadWriteServiceImpl<LikeId, Like>  implements LikeService{

    private final LikeDao likeDao;

    public LikeServiceImpl(LikeDao likeDao) {
        super(likeDao);
        this.likeDao = likeDao;
    }

}
