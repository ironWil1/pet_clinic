package com.vet24.web.controllers.user;


import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.user.CommentDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.user.CommentMapper;
import com.vet24.models.user.Comment;
import com.vet24.models.user.CommentReaction;
import com.vet24.models.user.User;
import com.vet24.models.util.View;
import com.vet24.service.user.CommentReactionService;
import com.vet24.service.user.CommentService;
import com.vet24.web.controllers.annotations.CheckExist;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.vet24.models.secutity.SecurityUtil.getOptionalOfNullableSecurityUser;


@RestController
@RequestMapping("api/user/comment/")
@Slf4j
@Tag(name = "User comment controller", description = "operations with comment reactions")
public class UserCommentController {

    private final CommentService commentService;
    private final CommentReactionService commentReactionService;
    private final CommentMapper commentMapper;

    public UserCommentController(CommentService commentService, CommentReactionService commentReactionService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentReactionService = commentReactionService;
        this.commentMapper = commentMapper;
    }

    @Operation(summary = "like or dislike a comment")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully liked/disliked the comment"),
            @ApiResponse(responseCode = "400", description = "Comment is not found")})
    @PutMapping(value = "/{commentId}/{positive}")
    public ResponseEntity<Void> likeOrDislikeComment(@CheckExist (entityClass = Comment.class) @PathVariable Long commentId,
                                                     @PathVariable boolean positive) {

        Comment comment = commentService.getByKey(commentId);
        return getOptionalOfNullableSecurityUser()
                .map(user -> new CommentReaction(comment, user, positive))
                .map(commentReaction -> {
                    log.info("The reaction on the comment was added as positive {}", commentReaction.getPositive());
                    return commentReactionService.update(commentReaction);
                })
                .map(x -> new ResponseEntity<Void>(HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    //TODO Добавить проверку на обновляемый коммент, что юзер является его автором
    @Operation(summary = "Update and return comment, changing only content")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Comment updated",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "400", description = "it's not your comment or Comment not found")})
    @PutMapping(value = "/{commentId}")
    public ResponseEntity<CommentDto> createOrUpdate(@CheckExist (entityClass = Comment.class) @PathVariable("commentId") Long commentId,
                                                     @JsonView(View.Put.class) @Valid @RequestBody CommentDto commentDto) {

        Comment comment = commentService.getByKey(commentId);
        getOptionalOfNullableSecurityUser().filter(user -> comment.getUser().equals(user))
                .orElseThrow(() -> new BadRequestException("it's not your comment"));

        comment.setContent(commentDto.getContent());
        commentService.update(comment);

        return ResponseEntity.ok(commentMapper.toDto(comment));
    }


    @Operation(summary = "Delete comment")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Comment deleted"),
            @ApiResponse(responseCode = "400", description = "Comment not found")})
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> removeComment(@CheckExist (entityClass = Comment.class) @PathVariable("commentId") Long commentId) {
        User user = getOptionalOfNullableSecurityUser().orElseThrow(() -> new BadRequestException("You are not authorized"));
        Comment comment = commentService.getByKey(commentId);
            if (comment.getUser().equals(user)) {
                commentService.delete(comment);
                log.info("Comment with id {} deleted", commentId);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        return ResponseEntity.ok().build();
    }
}
