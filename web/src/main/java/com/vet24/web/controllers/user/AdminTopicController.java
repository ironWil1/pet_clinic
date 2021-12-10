package com.vet24.web.controllers.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.dto.user.TopicDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.user.TopicMapper;
import com.vet24.models.user.Topic;
import com.vet24.models.util.View;
import com.vet24.service.user.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

@RestController
@RequestMapping(value = "api/admin/topic")
@Tag(name = "admin topic controller", description = "adminTopicController operations")
public class AdminTopicController {

    private final TopicService topicService;
    private final TopicMapper topicMapper;

    @Autowired
    public AdminTopicController(TopicService topicService, TopicMapper topicMapper) {
        this.topicService = topicService;
        this.topicMapper = topicMapper;
    }

    @Operation(summary = "Deleting a topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "topic removed"),
            @ApiResponse(responseCode = "404", description = "topic not found")
    })
    @DeleteMapping("{topicId}")
    public ResponseEntity<Void> deleteTopic(@PathVariable("topicId") Long id) {
        Topic topic = topicService.getByKey(id);
        if (topic != null) topicService.delete(topic);
        else throw new NotFoundException("topic not found");
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Closing an open topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = " the topic is close"),
            @ApiResponse(responseCode = "404", description = "topic not found")
    })
    @PutMapping("{topicId}/close")
    public ResponseEntity<Void> closeTopic(@PathVariable("topicId") Long id) {
        Topic topic = topicService.getByKey(id);
        if (topic != null) {
            if (!topic.isClosed()) {
                topic.setClosed(true);
                topicService.update(topic);
                return ResponseEntity.ok().build();
            } else throw new BadRequestException("Топик является закрытым");
        } else throw new NotFoundException("Топик не найден");
    }

    @Operation(summary = "Opening a closed topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the topic is open"),
            @ApiResponse(responseCode = "404", description = "topic not found")
    })
    @PutMapping("{topicId}/open")
    public ResponseEntity<Void> openTopic(@PathVariable("topicId") Long id) {
        Topic topic = topicService.getByKey(id);
        if (topic != null) {
            if (topic.isClosed()) {
                topic.setClosed(false);
                topicService.update(topic);
                return ResponseEntity.ok().build();
            } else throw new BadRequestException("Топик является открытым");
        } else throw new NotFoundException("Топик не найден");
    }

    @Operation(summary = "update info from topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "topic is update",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "404", description = "topic not found")
    })
    @PutMapping("/{topicId}")
    public ResponseEntity<TopicDto> updateTopic(@PathVariable("topicId") Long topicId, @JsonView(View.Put.class)
                                                @Validated(OnUpdate.class) @RequestBody(required = false) TopicDto topicDto) {
        if (!topicService.isExistByKey(topicId)) throw new NotFoundException("topic not found");
        Topic topic = topicService.getByKey(topicId);
        topic.setTitle(topicDto.getTitle());
        topic.setContent(topicDto.getContent());
        topicService.update(topic);
        return ResponseEntity.ok(topicMapper.toDto(topic));
    }
}
