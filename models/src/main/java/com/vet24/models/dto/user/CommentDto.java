package com.vet24.models.dto.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.util.Get;
import com.vet24.models.util.Put;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class CommentDto {

    @JsonView(Get.class)
    private Long id;

    @JsonView({Get.class, Put.class})
    @NotBlank
    private String content;

    @JsonView(Get.class)
    private LocalDateTime dateTime;

    @JsonView(Get.class)
    private int likes;

    @JsonView(Get.class)
    private int dislike;

    @JsonView(Get.class)
    private UserInfoDto userInfoDto;
}
