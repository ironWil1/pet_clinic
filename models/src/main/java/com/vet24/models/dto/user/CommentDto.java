package com.vet24.models.dto.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.util.View;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class CommentDto {

    @JsonView(View.Get.class)
    private Long id;

    @JsonView({View.Get.class, View.Put.class})
    @NotBlank
    private String content;

    @JsonView(View.Get.class)
    private LocalDateTime dateTime;

    @JsonView(View.Get.class)
    private int likes;

    @JsonView(View.Get.class)
    private int dislike;

    @JsonView(View.Get.class)
    private UserInfoDto userInfoDto;
}
