package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.user.DoctorReview;

import java.util.List;

public interface DoctorReviewDao extends ReadWriteDao<Long, DoctorReview> {
    DoctorReview getByDoctorAndClientId(long doctorId, long clientId);
    List<DoctorReview> getAllReviewByDoctorId(long doctorId);
}
