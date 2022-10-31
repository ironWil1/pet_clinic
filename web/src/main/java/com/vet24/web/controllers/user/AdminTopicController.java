package com.vet24.web.controllers.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.dto.user.TopicDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.user.TopicMapper;
import com.vet24.models.user.Topic;
import com.vet24.models.util.View;
import com.vet24.service.user.TopicService;
import com.vet24.web.controllers.annotations.CheckExist;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
            @ApiResponse(responseCode = "400", description = "topic not found")
    })
    @DeleteMapping("{topicId}")
    public ResponseEntity<Void> deleteTopic(@CheckExist (entityClass = Topic.class)@PathVariable("topicId") Long id) {
        Topic topic = topicService.getByKey(id);
        topicService.delete(topic);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Closing an open topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the topic is close"),
            @ApiResponse(responseCode = "400", description = "topic not found or is closed")
    })
    @PutMapping("{topicId}/close")
    public ResponseEntity<Void> closeTopic(@CheckExist(entityClass = Topic.class)@PathVariable("topicId") Long id) {
        Topic topic = topicService.getByKey(id);
            if (!topic.isClosed()) {
                topic.setClosed(true);
                topicService.update(topic);
                return ResponseEntity.ok().build();
            } else throw new BadRequestException("Топик является закрытым");
        }

    @Operation(summary = "Opening a closed topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the topic is open"),
            @ApiResponse(responseCode = "400", description = "topic not found or is opened")
    })
    @PutMapping("{topicId}/open")
    public ResponseEntity<Void> openTopic(@CheckExist(entityClass = Topic.class) @PathVariable("topicId") Long id) {
        Topic topic = topicService.getByKey(id);
            if (topic.isClosed()) {
                topic.setClosed(false);
                topicService.update(topic);
                return ResponseEntity.ok().build();
            } else throw new BadRequestException("Топик является открытым");
        }

    @Operation(summary = "update info from topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "topic is update",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "400", description = "topic not found")
    })
    @PutMapping("/{topicId}")
    public ResponseEntity<TopicDto> updateTopic
            (@CheckExist(entityClass = Topic.class) @PathVariable("topicId") Long topicId, @JsonView(View.Put.class)
    @Validated(OnUpdate.class) @RequestBody(required = false) TopicDto topicDto) {
        Topic topic = topicService.getByKey(topicId);
        topic.setTitle(topicDto.getTitle());
        topic.setContent(topicDto.getContent());
        topicService.update(topic);
        return ResponseEntity.ok(topicMapper.toDto(topic));
    }

    @Operation(summary = "get all topics from base")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful receipt of topics from base",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopicDto.class)))
    })
    @GetMapping("/allTopics")
    public ResponseEntity<List<TopicDto>> getAllTopics() {
        List<TopicDto> topicDtoList = topicMapper.toDto(topicService.getAll());
        return new ResponseEntity<>(topicDtoList, HttpStatus.OK);
    }

    @Operation(summary = "get topic by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful getting topic",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "400", description = "Topic not found")
    })
    @GetMapping("/{topicId}")
    public ResponseEntity<TopicDto> getTopicById (@CheckExist(entityClass = Topic.class) @PathVariable("topicId") Long topicId) {
        return new ResponseEntity<>(topicMapper.toDto(topicService.getByKey(topicId)), HttpStatus.OK);
    }
}
