package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.Comment;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class CommentDaoImpl extends ReadWriteDaoImpl<Long, Comment>  implements CommentDao{

    @Override
    public Comment findByUserId(long userId) {
        try {
            return manager.createQuery("SELECT c FROM Comment c WHERE c.user.id =:userId", Comment.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
