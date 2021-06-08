package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.Comment;
import com.vet24.models.user.Like;
import com.vet24.models.user.LikeId;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class LikeDaoImpl extends ReadWriteDaoImpl<LikeId, Like> implements LikeDao{

    @Override
    public List<Like> findByClientId(Long clientId) {
        return manager.createQuery("SELECT l FROM Like l WHERE l.likeId.client.id =:clientId ", Like.class)
                .setParameter("clientId", clientId)
                .getResultList();
    }
}
