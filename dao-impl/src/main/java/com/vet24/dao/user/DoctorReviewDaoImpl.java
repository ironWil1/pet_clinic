package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.Comment;
import com.vet24.models.user.DoctorReview;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class DoctorReviewDaoImpl extends ReadWriteDaoImpl<Long, DoctorReview>  implements DoctorReviewDao{

    @Override
    public DoctorReview findByDoctorId(long doctorId) {
        try {
            return manager.createQuery("SELECT c FROM DoctorReview c WHERE c.doctor.id =:doctorId", DoctorReview.class)
                    .setParameter("doctorId", doctorId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
