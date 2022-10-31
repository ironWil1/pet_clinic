package com.vet24.web.controllers.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.user.CommentDto;
import com.vet24.models.mappers.user.CommentMapper;
import com.vet24.models.user.Comment;
import com.vet24.models.util.View;
import com.vet24.service.user.CommentService;
import com.vet24.web.controllers.annotations.CheckExist;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/admin/comment/")
@Tag(name = "admin comment controller", description = "adminCommentController operations")
@Slf4j
public class AdminCommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @Autowired
    public AdminCommentController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @Operation(summary = "Update comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "400", description = "Comment not found")
    })
    @PutMapping("{id}")
    public ResponseEntity<CommentDto> updateComment(@JsonView(View.Put.class)
                                                    @Valid @RequestBody CommentDto commentDto,
                                                    @CheckExist (entityClass = Comment.class) @PathVariable("id") Long id) {
            Comment comment = commentService.getByKey(id);
            comment.setContent(commentDto.getContent());
            commentService.update(comment);
            log.info("Comment with id {} updated", id);
            return ResponseEntity.ok(commentMapper.toDto(comment));
    }

    @Operation(summary = "Delete comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted"),
            @ApiResponse(responseCode = "400", description = "Comment not found")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteComment(@CheckExist (entityClass = Comment.class) @PathVariable("id") Long id) {
        Comment comment = commentService.getByKey(id);
            commentService.delete(comment);
            log.info("Comment with id {} deleted", id);
        return ResponseEntity.ok().build();
    }

}
