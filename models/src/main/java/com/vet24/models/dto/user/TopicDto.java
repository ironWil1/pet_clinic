package com.vet24.models.dto.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.util.View;
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

    @JsonView(View.Public1.class)
    @Null(groups = {OnCreate.class})
    @NotNull(groups = {OnUpdate.class})
    private Long id;

    @JsonView({View.Public1.class, View.Public2.class})
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String title;

    @JsonView({View.Public1.class, View.Public2.class})
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String content;

    @JsonView(View.Ignore.class)
    private LocalDateTime creationDate;

    @JsonView(View.Ignore.class)
    private LocalDateTime lastUpdateDate;

    @JsonView(View.Ignore.class)
    private UserInfoDto topicStarter;

    @JsonView(View.Ignore.class)
    private List<CommentDto> commentDtoList;
}
