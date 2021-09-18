package com.vet24.web.controllers.user;

import com.vet24.models.dto.user.CommentDto;
import com.vet24.models.mappers.user.CommentMapper;
import com.vet24.models.user.Comment;
import com.vet24.service.user.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "api/admin/comment/")
@Tag(name = "admin comment controller", description = "adminCommentController operations")

public class AdminCommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @Autowired
    public AdminCommentController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @SneakyThrows
    @Operation(summary = "Update comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @PutMapping("{id}")
    public ResponseEntity<CommentDto> updateComment(@RequestBody CommentDto commentDto,
                                                    @PathVariable("id") Long id) {
        if (commentService.isExistByKey(id)) {
            Comment comment = commentService.getByKey(id);
            comment.setContent(commentDto.getContent());
            commentService.update(comment);

            return ResponseEntity.ok(commentMapper.toDto(comment));
        } else {
            throw new NotFoundException("Comment not found");
        }
    }

    @SneakyThrows
    @Operation(summary = "Delete comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id) {
        Comment comment = commentService.getByKey(id);
        if (comment != null) {
            commentService.delete(comment);
        } else {
            throw new NotFoundException("Comment not found");
        }
        return ResponseEntity.ok().build();
    }
}
