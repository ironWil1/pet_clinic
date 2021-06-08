package com.vet24.service.user;

import com.vet24.dao.user.LikeDao;
import com.vet24.models.user.Comment;
import com.vet24.models.user.Like;
import com.vet24.models.user.LikeId;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;

@Service
public class LikeServiceImpl extends ReadWriteServiceImpl<LikeId, Like>  implements LikeService{

    private final LikeDao likeDao;

    public LikeServiceImpl(LikeDao likeDao) {
        super(likeDao);
        this.likeDao = likeDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Like> findByClientId(Long clientId) {
        return likeDao.findByClientId(clientId);
    }
}
