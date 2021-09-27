package com.vet24.models.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class CommentDto {

    private Long id;
    private UserInfoDto UserInfoDto;
    @NotBlank
    private String content;
    private LocalDateTime dateTime;
    private int likes;
    private int dislike;
}
