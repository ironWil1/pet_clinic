package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.dto.user.DoctorReviewDto;
import com.vet24.models.user.DoctorReview;
import com.vet24.models.user.Topic;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class DoctorReviewDaoImpl extends ReadWriteDaoImpl<Long, DoctorReview>  implements DoctorReviewDao{

    @Override
    public DoctorReview getByDoctorAndClientId(long doctorId, long userId) {
        try {
            return manager.createQuery("SELECT dr FROM DoctorReview dr WHERE dr.doctor.id =:doctorId AND dr.comment.user.id=:userId", DoctorReview.class)
                    .setParameter("doctorId", doctorId)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<DoctorReview> getAllReviewByDoctorId(long doctorId) {
        return manager.createQuery("SELECT review FROM DoctorReview review WHERE review.doctor.id =:doctorId", DoctorReview.class)
                .setParameter("doctorId", doctorId)
                .getResultList();
    }
}
