package com.vet24.models.dto.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    private Long id;
    private Long userId;
    private String comment;
    private LocalDateTime dateTime;

}
