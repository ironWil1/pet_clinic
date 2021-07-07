package com.vet24.web.controllers.user;

import com.vet24.models.exception.RepeatedCommentException;
import com.vet24.models.user.Client;
import com.vet24.models.user.Comment;
import com.vet24.models.user.CommentReaction;
import com.vet24.models.user.Doctor;
import com.vet24.service.user.ClientService;
import com.vet24.service.user.CommentReactionService;
import com.vet24.service.user.CommentService;
import com.vet24.service.user.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/api/client")
@Tag(name = "doctor-controller", description = "operations with doctors")
public class ClientCommentController {

    private final DoctorService doctorService;
    private final ClientService clientService;
    private final CommentService commentService;
    private final CommentReactionService commentReactionService;

    public ClientCommentController(DoctorService doctorService, ClientService clientService, CommentService commentService, CommentReactionService commentReactionService) {
        this.doctorService = doctorService;
        this.clientService = clientService;
        this.commentService = commentService;
        this.commentReactionService = commentReactionService;
    }

    @Operation(summary = "add comment by Client for Doctor")
    @PostMapping(value = "/doctor/{doctorId}/addComment")
    public ResponseEntity<String> persistComment(@PathVariable("doctorId") Long doctorId, String text){
        Doctor doctor = doctorService.getByKey(doctorId);
        if (doctor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Comment comment = null;
            Client currentClient = clientService.getCurrentClient();
            Long clientId = currentClient.getId();
            if (commentService.findByClientIdAndDoctorId(clientId, doctorId) == null) {
                comment = new Comment(
                        clientService.getCurrentClient(), text, LocalDateTime.now(), doctor
                );
                log.info("The comment {} was added to Doctor with id {}",text,doctorId);
                commentService.persist(comment);
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
    public ResponseEntity<Void> likeOrDislikeComment(@PathVariable Long commentId, @PathVariable boolean positive)  {

        Client client = clientService.getCurrentClient();
        Comment comment = commentService.getByKey(commentId);
        if (comment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        CommentReaction commentLike = new CommentReaction(comment,client,positive);
        commentReactionService.update(commentLike);
        log.info("The reaction on the comment was added as positive {}",commentLike.getPositive());
        return new  ResponseEntity<>(HttpStatus.OK);
    }

}
