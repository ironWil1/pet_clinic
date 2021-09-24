package com.vet24.models.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class CommentDto {

    private Long id;
    @NotNull
    private UserInfoDto UserInfoDto;
    @NotBlank
    @Size(min = 15)
    private String content;
    private LocalDateTime dateTime;
    private int likes;
    private int dislike;
}
