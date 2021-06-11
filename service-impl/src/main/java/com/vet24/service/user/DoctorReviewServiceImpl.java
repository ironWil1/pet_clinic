package com.vet24.service.user;

import com.vet24.dao.user.CommentDao;
import com.vet24.dao.user.DoctorReviewDao;
import com.vet24.models.user.Comment;
import com.vet24.models.user.DoctorReview;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorReviewServiceImpl extends ReadWriteServiceImpl<Long, DoctorReview> implements DoctorReviewService {

    private final DoctorReviewDao doctorReviewDao;

    @Autowired
    public DoctorReviewServiceImpl(DoctorReviewDao doctorReviewDao) {
        super(doctorReviewDao);
        this.doctorReviewDao = doctorReviewDao;
    }

    @Override
    public DoctorReview findViewByDoctorIdAndClientId(long doctorId, long clientId) {
        return doctorReviewDao.findViewByDoctorIdAndClientId(doctorId, clientId);
    }
}
