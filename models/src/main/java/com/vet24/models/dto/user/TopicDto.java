package com.vet24.models.dto.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.util.Get;
import com.vet24.models.util.Post;
import com.vet24.models.util.Put;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicDto {

    @JsonView({Put.class, Get.class})
    @Null(groups = {OnCreate.class})
    @NotNull(groups = {OnUpdate.class})
    private Long id;

    @JsonView({Put.class, Post.class, Get.class})
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String title;

    @JsonView({Put.class, Post.class, Get.class})
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String content;

    @JsonView(Get.class)
    private LocalDateTime creationDate;

    @JsonView(Get.class)
    private LocalDateTime lastUpdateDate;

    @JsonView(Get.class)
    private UserInfoDto topicStarter;

    @JsonView(Get.class)
    private List<CommentDto> commentDtoList;
}