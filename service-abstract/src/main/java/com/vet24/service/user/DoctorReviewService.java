package com.vet24.service.user;

import com.vet24.models.user.Comment;
import com.vet24.models.user.DoctorReview;
import com.vet24.service.ReadWriteService;

public interface DoctorReviewService extends ReadWriteService<Long, DoctorReview> {
    Comment findByDoctorId(long doctorId);
}
