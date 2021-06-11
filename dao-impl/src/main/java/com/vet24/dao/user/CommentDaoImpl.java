package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.Comment;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class CommentDaoImpl extends ReadWriteDaoImpl<Long, Comment>  implements CommentDao{

    @Override
    public Comment findByClientIdAndDoctorId(long clientId, long doctorId) {
        try {
            return manager.createQuery("SELECT c FROM Comment c WHERE c.client.id =:clientId AND c.doctor.id =:doctorId", Comment.class)
                    .setParameter("clientId", clientId)
                    .setParameter("doctorId", doctorId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}

