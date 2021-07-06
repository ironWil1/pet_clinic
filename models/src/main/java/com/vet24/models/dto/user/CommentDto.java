package com.vet24.models.dto.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    private Long id;
    private UserInfoDto UserInfoDto;
    private String content;
    private LocalDateTime dateTime;
    private int likes;
    private int dislike;

}
