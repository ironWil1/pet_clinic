package com.vet24.service.user;

import com.vet24.models.user.DoctorReview;
import com.vet24.service.ReadWriteService;

import java.util.List;

public interface DoctorReviewService extends ReadWriteService<Long, DoctorReview> {
    DoctorReview getByDoctorAndClientId(long doctorId, long clientId);
    List<DoctorReview> getAllReviewByDoctorId(long doctorId);
}
