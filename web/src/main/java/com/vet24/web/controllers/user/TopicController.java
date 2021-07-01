package com.vet24.web.controllers.user;

import com.vet24.models.dto.user.CommentDto;
import com.vet24.models.mappers.user.CommentMapper;
import com.vet24.models.user.Comment;
import com.vet24.models.user.Topic;
import com.vet24.service.user.TopicService;
import com.vet24.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/api/user/topic")
public class TopicController {

    private final TopicService topicService;
    private final UserService userService;
    private final CommentMapper commentMapper;

    @Autowired
    public TopicController(TopicService topicService,
                           UserService userService,
                           CommentMapper commentMapper) {
        this.topicService = topicService;
        this.userService = userService;
        this.commentMapper = commentMapper;
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
                                               @RequestBody(required = false) String content) {
        if ("".equals(content)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Topic topic = topicService.getByKey(topicId);
        if (topic == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (topic.isClosed()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Comment comment = new Comment(userService.getCurrentUser(), content, LocalDateTime.now());
        topic.getComments().add(comment);
        topicService.persist(topic);

        CommentDto commentDto = commentMapper.toDto(comment);

        return new ResponseEntity<>(commentDto, HttpStatus.CREATED);
    }
}
