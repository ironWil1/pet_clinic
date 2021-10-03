package com.vet24.web.controllers.user;

import com.vet24.models.dto.user.CommentDto;
import com.vet24.models.exception.RepeatedCommentException;
import com.vet24.models.mappers.user.CommentMapper;
import com.vet24.models.user.Client;
import com.vet24.models.user.Comment;
import com.vet24.models.user.CommentReaction;
import com.vet24.models.user.Doctor;
import com.vet24.models.user.DoctorReview;
import com.vet24.models.user.User;
import com.vet24.service.user.ClientService;
import com.vet24.service.user.CommentReactionService;
import com.vet24.service.user.CommentService;
import com.vet24.service.user.DoctorReviewService;
import com.vet24.service.user.DoctorService;
import com.vet24.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/api/client/doctor")
@Tag(name = "сlient review сontroller", description = "operations with comments")
public class ClientReviewController {

    private final DoctorService doctorService;
    private final ClientService clientService;
    private final CommentService commentService;
    private final DoctorReviewService doctorReviewService;
    private final UserService userService;
    private final CommentReactionService commentReactionService;
    private final CommentMapper commentMapper;

    @Autowired
    public ClientReviewController(DoctorService doctorService, ClientService clientService, CommentService commentService, DoctorReviewService doctorReviewService, UserService userService, CommentReactionService commentReactionService, CommentMapper commentMapper) {
        this.doctorService = doctorService;
        this.clientService = clientService;
        this.commentService = commentService;
        this.doctorReviewService = doctorReviewService;
        this.userService = userService;
        this.commentReactionService = commentReactionService;
        this.commentMapper = commentMapper;
    }

    @Operation(summary = "add comment by Client for Doctor")
    @PostMapping(value = "/{doctorId}/review")
    public ResponseEntity<String> persistComment(@PathVariable("doctorId") Long doctorId, String text) {
        Doctor doctor = doctorService.getByKey(doctorId);
        if (doctor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Comment comment = null;
            DoctorReview doctorReview = null;
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userId = currentUser.getId();
            if (doctorReviewService.getByDoctorAndClientId(doctorId, userId) == null) {
                comment = new Comment(
                        currentUser, text, LocalDateTime.now()
                );
                doctorReview = new DoctorReview(comment, doctor);

                log.info("The comment {} was added to Doctor with id {}", text, doctorId);
                commentService.persist(comment);

                doctorReviewService.persist(doctorReview);
            } else {
                log.info("The comment is not correct");
                throw new RepeatedCommentException("You can add only one comment to Doctor. So you have to update or delete old one.");
            }
            return new ResponseEntity<>(comment.getContent(), HttpStatus.OK);
        }
    }


    @Operation(summary = "like or dislike a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully liked/disliked the comment"),
            @ApiResponse(responseCode = "404", description = "Comment is not found")
    })
    @PostMapping(value = "/{commentId}/{positive}")
    public ResponseEntity<Void> likeOrDislikeComment(@PathVariable Long commentId, @PathVariable boolean positive) {

        Comment comment = commentService.getByKey(commentId);
        if (comment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CommentReaction commentLike = new CommentReaction(comment, client, positive);
        commentReactionService.update(commentLike);
        log.info("The reaction on the comment was added as positive {}", commentLike.getPositive());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "update a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "400", description = "Another client's comment")
    })
    @PutMapping(value = "/{doctorId}/review")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("doctorId") Long doctorId, @RequestBody String text) {
        Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DoctorReview doctorReview = doctorReviewService.getByDoctorAndClientId(doctorId, client.getId());
        if (doctorReview == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!(doctorReview.getComment().getUser().getId().equals(client.getId()))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        doctorReview.getComment().setContent(text);
        doctorReviewService.update(doctorReview);
        log.info("We updated comment with this id {}", doctorReview.getComment().getId());
        return ResponseEntity.ok().body(commentMapper.toDto(doctorReview.getComment()));
    }

    @Operation(summary = "delete a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment delete"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "400", description = "Another client's comment")
    })
    @DeleteMapping(value = "/{doctorId}/review")
    public ResponseEntity<Void> deleteComment(@PathVariable("doctorId") Long doctorId) {
        Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DoctorReview doctorReview = doctorReviewService.getByDoctorAndClientId(doctorId, client.getId());
        if (doctorReview == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!(doctorReview.getComment().getUser().getId().equals(client.getId()))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        doctorReviewService.delete(doctorReview);
        log.info("We deleted comment with this id {}", doctorReview.getComment().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
