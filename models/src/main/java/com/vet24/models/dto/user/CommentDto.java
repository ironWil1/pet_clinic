package com.vet24.models.dto.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.util.View;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class CommentDto {

    @JsonView(View.Ignore.class)
    private Long id;

    @JsonView(View.Ignore.class)
    private UserInfoDto UserInfoDto;

    @NotBlank
    private String content;

    @JsonView(View.Ignore.class)
    private LocalDateTime dateTime;

    @JsonView(View.Ignore.class)
    private int likes;

    @JsonView(View.Ignore.class)
    private int dislike;
}
