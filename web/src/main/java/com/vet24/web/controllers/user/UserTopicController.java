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
import com.vet24.service.user.CommentService;
import com.vet24.service.user.TopicService;
import com.vet24.service.user.UserService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

import static com.vet24.models.secutity.SecurityUtil.getOptionalOfNullableSecurityUser;


//TODO: Убрать NPE в getAllTopics
@RestController
@RequestMapping(value = "/api/user/topic")
@Tag(name = "Topic controller", description = "operation with topic")
public class UserTopicController {

    private final UserService userService;
    private final TopicService topicService;
    private final TopicMapper topicMapper;
    private final CommentService commentService;
    private final CommentMapper commentMapper;


    @Autowired
    public UserTopicController(UserService userService, TopicService topicService, TopicMapper topicMapper, CommentService commentService, CommentMapper commentMapper) {
        this.userService = userService;
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
        return getOptionalOfNullableSecurityUser().map(User::getId)
                .map(topicService::getTopicByClientId)
                .map(topicMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Topics are not found"));
    }

    @Operation(summary = "get topic by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful getting topic",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "400", description = "Topic not found")
    })
    @GetMapping("/{topicId}")
    public ResponseEntity<TopicDto> getTopicById(@CheckExist (entityClass = Topic.class) @PathVariable("topicId") Long topicId) {
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
        topic.setTopicStarter(getOptionalOfNullableSecurityUser().orElseThrow(() -> new NotFoundException("user not found")));
        topic.setCreationDate(LocalDateTime.now());
        topic.setLastUpdateDate(LocalDateTime.now());
        topicService.persist(topic);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "update info from topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "topic is update",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "404", description = "is not your topic"),
            @ApiResponse(responseCode = "400", description = "topic not found")
    })
    @PutMapping("/{topicId}")
    public ResponseEntity<TopicDto> updateTopic(@CheckExist (entityClass = Topic.class) @PathVariable("topicId") Long topicId, @JsonView(View.Put.class)
    @RequestBody(required = false) TopicDto topicDto) {

        Topic topic = topicService.getByKey(topicId);
        getOptionalOfNullableSecurityUser().filter(topic.getTopicStarter()::equals)
                .orElseThrow(() -> new NotFoundException("it's not your topic"));

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
            @ApiResponse(responseCode = "400", description = "topic not found or is not your topic")
    })
    @DeleteMapping("/{topicId}")
    public ResponseEntity<Void> deleteTopic(@CheckExist(entityClass = Topic.class)@PathVariable("topicId") Long topicId) {

        Topic topic = topicService.getByKey(topicId);
        getOptionalOfNullableSecurityUser()
                .filter(topic.getTopicStarter()::equals)
                .orElseThrow(() -> new NotFoundException("it's not your topic"));

        topicService.delete(topic);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "add comment to topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "comment created"),
            @ApiResponse(responseCode = "400", description = "comment bad request or topic not found"),
            @ApiResponse(responseCode = "403", description = "topic is closed")
    })
    @PostMapping(value = "/{topicId}/addComment")
    public ResponseEntity<CommentDto> persistTopicComment(@CheckExist (entityClass = Topic.class) @PathVariable("topicId") Long topicId,
                                                          @NotBlank(message = "{registration.validation.blank.field}")
                                                          @Size(min = 15)
                                                          @RequestBody String content) {
        Topic topic = topicService.getByKey(topicId);
        if (topic.isClosed()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Comment comment = new Comment(userService.getCurrentUser(), content);
        commentService.persist(comment);
        topic.getComments().add(comment);
        topicService.persist(topic);
        CommentDto commentDto = commentMapper.toDto(comment);

        return new ResponseEntity<>(commentDto, HttpStatus.CREATED);
    }
}
