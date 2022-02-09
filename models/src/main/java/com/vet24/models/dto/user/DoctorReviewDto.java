package com.vet24.models.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data@AllArgsConstructor
public class DoctorReviewDto {
    private Long id;
    private Long doctorId;
    private CommentDto review;
}
