package com.vet24.models.dto.user;

import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicDto {

    @Null(groups = {OnCreate.class})
    @NotNull(groups = {OnUpdate.class})
    private Long id;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private String title;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private String content;

    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;
    private UserInfoDto topicStarter;
    private List<CommentDto> commentDtoList;
}
