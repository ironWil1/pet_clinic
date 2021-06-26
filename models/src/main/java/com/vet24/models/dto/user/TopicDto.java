package com.vet24.models.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;
    private Long topicStarterId;
    private List<CommentDto> commentDtoList;
}
