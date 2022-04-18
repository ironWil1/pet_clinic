package com.vet24.web.controllers.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.user.CommentDto;
import com.vet24.models.dto.user.TopicDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.user.CommentMapper;
import com.vet24.models.mappers.user.TopicMapper;
import com.vet24.models.user.Comment;
import com.vet24.models.user.Topic;
import com.vet24.models.user.User;
import com.vet24.models.util.View;
import com.vet24.service.user.ClientService;
import com.vet24.service.user.CommentService;
import com.vet24.service.user.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/api/user/topic")
@Tag(name = "Topic controller", description = "operation with topic")
public class UserTopicController {

    private final ClientService clientService;
    private final TopicService topicService;
    private final TopicMapper topicMapper;
    private final CommentService commentService;
    private final CommentMapper commentMapper;


    @Autowired
    public UserTopicController(ClientService clientService, TopicService topicService, TopicMapper topicMapper, CommentService commentService, CommentMapper commentMapper) {
        this.clientService = clientService;
        this.topicService = topicService;
        this.topicMapper = topicMapper;
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @Operation(summary = "get all topics from base")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful receipt of topics from base",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "404", description = "Topics not found")
    })
    @GetMapping("/allTopics")
    public ResponseEntity<List<TopicDto>> getAllTopics() {
        List<TopicDto> topicDtoList = topicMapper.toDto(topicService.getAll());
        return new ResponseEntity<>(topicDtoList, HttpStatus.OK);
    }

    @Operation(summary = "get all topics from current client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful receipt of topics from current client",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "404", description = "topics are not found")
    })
    @GetMapping("/yourTopics")
    public ResponseEntity<List<TopicDto>> getAllClientTopic() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(topicMapper.toDto(topicService.getTopicByClientId(user.getId())), HttpStatus.OK);
    }

    @Operation(summary = "get topic by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful getting topic",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "404", description = "Topic not found")
    })
    @GetMapping("/{topicId}")
    public ResponseEntity<TopicDto> getTopicById(@PathVariable("topicId") Long topicId) {
        if (!topicService.isExistByKey(topicId)) {
            throw new NotFoundException("topic not found");
        }
        return new ResponseEntity<>(topicMapper.toDto(topicService.getByKey(topicId)), HttpStatus.OK);
    }

    @Operation(summary = "create topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "topic is create",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "400", description = "title or content can't null")
    })
    @PostMapping()
    public ResponseEntity<Void> createTopic(@JsonView(View.Post.class)
                                            @RequestBody(required = false) TopicDto topicDto) {
        if (topicDto.getTitle().trim().equals("") || topicDto.getContent().trim().equals("")) {
            throw new BadRequestException("title or content can't null");
        }
        Topic topic = topicMapper.toEntity(topicDto);
        topic.setTopicStarter((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        topic.setCreationDate(LocalDateTime.now());
        topic.setLastUpdateDate(LocalDateTime.now());
        topicService.persist(topic);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "update info from topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "topic is update",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "404", description = "topic not found or is not your topic")
    })
    @PutMapping("/{topicId}")
    public ResponseEntity<TopicDto> updateTopic(@PathVariable("topicId") Long topicId, @JsonView(View.Put.class)
                                                @RequestBody(required = false) TopicDto topicDto) {
        if (!topicService.isExistByKey(topicId)) {
            throw new NotFoundException("topic not found");
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Topic topic = topicService.getByKey(topicId);

        if (!topic.getTopicStarter().equals(user)) {
            throw new NotFoundException("it's not your topic");
        }

        if (!(topicDto.getTitle() == null || topicDto.getTitle().trim().equals(""))) {
            topic.setTitle(topicDto.getTitle());
        }
        if (!(topicDto.getContent() == null || topicDto.getContent().trim().equals(""))) {
            topic.setContent(topicDto.getContent());
        }
        topicService.update(topic);
        return new ResponseEntity<>(topicMapper.toDto(topic), HttpStatus.OK);
    }

    @Operation(summary = "delete topic by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "topic is delete"),
            @ApiResponse(responseCode = "404", description = "topic not found or is not your topic")
    })
    @DeleteMapping("/{topicId}")
    public ResponseEntity<Void> deleteTopic(@PathVariable("topicId") Long topicId) {
        if (!topicService.isExistByKey(topicId)) {
            throw new NotFoundException("topic is not found");
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Topic topic = topicService.getByKey(topicId);
        if (!topic.getTopicStarter().equals(user)) {
            throw new NotFoundException("it's not you topic");
        }
        topicService.delete(topic);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "add comment to topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "comment created"),
            @ApiResponse(responseCode = "400", description = "comment bad request"),
            @ApiResponse(responseCode = "404", description = "topic not found"),
            @ApiResponse(responseCode = "403", description = "topic is closed")
    })
    @PostMapping(value = "/{topicId}/addComment")
    public ResponseEntity<CommentDto> persistTopicComment(@PathVariable("topicId") Long topicId,
                                                          @NotBlank(message = "{registration.validation.blank.field}")
                                                          @Size(min = 15)
                                                          @RequestBody String content) {
        Topic topic = topicService.getByKey(topicId);
        if (topic == null) {
            throw new NotFoundException("topic is not found");
        }
        if (topic.isClosed()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Comment comment = new Comment(clientService.getCurrentClientWithPets(), content);
        commentService.persist(comment);
        topic.getComments().add(comment);
        topicService.persist(topic);
        CommentDto commentDto = commentMapper.toDto(comment);

        return new ResponseEntity<>(commentDto, HttpStatus.CREATED);
    }
}
