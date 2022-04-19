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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import javax.validation.Valid;

import static com.vet24.models.secutity.SecurityUtil.getPrincipalOrNull;

@RestController
@RequestMapping("api/user/comment/")
@Slf4j
@Tag(name = "User comment сontroller", description = "operations with comment reactions")
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully liked/disliked the comment"),
            @ApiResponse(responseCode = "404", description = "Comment is not found")
    })
    @PutMapping(value = "/{commentId}/{positive}")
    public ResponseEntity<Void> likeOrDislikeComment(@PathVariable Long commentId, @PathVariable boolean positive) {

        Comment comment = commentService.getByKey(commentId);
        if (comment == null) {
            throw new NotFoundException("Comment is not found");
        }

//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) getPrincipalOrNull();
        CommentReaction commentLike = new CommentReaction(comment, user, positive);
        commentReactionService.update(commentLike);
        log.info("The reaction on the comment was added as positive {}", commentLike.getPositive());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //TODO Добавить проверку на обновляемый коммент, что юзер является его автором
    @Operation(summary = "Update and return comment, changing only content")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "400", description = "it's not your comment"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
    })
    @PutMapping(value = "/{commentId}")
    public ResponseEntity<CommentDto> createOrUpdate(@PathVariable("commentId") Long commentId,
                                                     @JsonView(View.Put.class)
                                                     @Valid @RequestBody CommentDto commentDto) {
        if (!commentService.isExistByKey(commentId)) {
            throw new NotFoundException("Comment not found");
        }

//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) getPrincipalOrNull();
        Comment comment = commentService.getByKey(commentId);

        if (!comment.getUser().equals(user)) {
            throw new BadRequestException("it's not your comment");
        }

        comment.setContent(commentDto.getContent());
        commentService.update(comment);

        return ResponseEntity.ok(commentMapper.toDto(comment));
    }


    @Operation(summary = "Delete comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> removeComment(@PathVariable("commentId") Long commentId) {
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) getPrincipalOrNull();
        Comment comment = commentService.getByKey(commentId);
        if (comment != null) {
            if (comment.getUser().equals(user)) {
                commentService.delete(comment);
                log.info("Comment with id {} deleted", commentId);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            log.info("Comment with id {} not found", commentId);
            throw new NotFoundException("Comment not found");
        }
        return ResponseEntity.ok().build();
    }
}
