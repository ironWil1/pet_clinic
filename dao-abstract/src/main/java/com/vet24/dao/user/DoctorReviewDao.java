package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.user.Comment;
import com.vet24.models.user.DoctorReview;

public interface DoctorReviewDao extends ReadWriteDao<Long, DoctorReview> {
    DoctorReview findByDoctorId(long doctorId);
}
