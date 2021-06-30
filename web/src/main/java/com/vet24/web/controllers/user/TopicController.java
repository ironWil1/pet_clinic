package com.vet24.web.controllers.user;

import com.vet24.models.user.Comment;
import com.vet24.models.user.Topic;
import com.vet24.models.user.User;
import com.vet24.service.user.TopicService;
import com.vet24.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

@RestController
@RequestMapping(value = "/api/user/topic")
public class TopicController {

    private final TopicService topicService;
    private final UserService userService;
    private final TopicCommentService topicCommentService;
    private final CommentMapper commentMapper;

    @Autowired
    public TopicController(TopicService topicService,
                           UserService userService,
                           TopicCommentService topicCommentService,
                           CommentMapper commentMapper) {
        this.topicService = topicService;
        this.userService = userService;
        this.topicCommentService = topicCommentService;
        this.commentMapper = commentMapper;
    }

    @Operation(summary = "add comment to topic")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "comment created",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDto.class))), /* // TODO: 01.07.2021 CommentDto */
        @ApiResponse(responseCode = "400", description = "comment bad request"),
        @ApiResponse(responseCode = "403", description = "topic is closed"),
        @ApiResponse(responseCode = "404", description = "topic not found")
    })
    @PostMapping(value = "/{topicId}/addComment")
    public ResponseEntity<Void> persistTopicComment(@PathVariable("topicId") Long topicId,
                                               @RequestBody(required = false) CommentDto commentDto) {
        if ("".equals(commentDto.getContent())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Topic topic = topicService.getByKey(topicId);
        if (topic == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (topic.isClosed()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        User currentUser = userService.getCurrentUser();
        Comment comment = commentMapper.toEntity(commentDto);   // TODO: 01.07.2021 currentUser, LocalDateTime, ...

        TopicComment topicComment = new TopicComment(topic, comment);
        topicCommentService.persist(topicComment);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
