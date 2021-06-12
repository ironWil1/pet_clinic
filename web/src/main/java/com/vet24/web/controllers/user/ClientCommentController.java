package com.vet24.web.controllers.user;

import com.vet24.models.exception.RepeatedCommentException;
import com.vet24.models.user.*;
import com.vet24.service.user.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/client/doctor")
@Tag(name = "doctor-controller", description = "operations with doctors")
public class ClientCommentController {

    private final DoctorService doctorService;
    private final ClientService clientService;
    private final CommentService commentService;
    private final DoctorReviewService doctorReviewService;
    private final UserService userService;

    @Autowired
    public ClientCommentController(DoctorService doctorService, ClientService clientService, CommentService commentService,DoctorReviewService doctorReviewService, UserService userService) {
        this.doctorService = doctorService;
        this.clientService = clientService;
        this.commentService = commentService;
        this.doctorReviewService = doctorReviewService;
        this.userService = userService;
    }

    @Operation(summary = "add comment by Client for Doctor")
    @PostMapping(value = "/{doctorId}/addComment")
    public ResponseEntity<String> persistComment(@PathVariable("doctorId") Long doctorId, String text){
        Doctor doctor = doctorService.getByKey(doctorId);
        if (doctor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Comment comment = null;
            DoctorReview doctorReview = null;
            User currentUser = userService.getCurrentUser();
            Long userId = currentUser.getId();
            if (doctorReviewService.findViewByDoctorIdAndClientId(doctorId,userId) == null) {
                comment = new Comment(
                        userService.getCurrentUser(), text, LocalDate.now()
                );
                doctorReview = new DoctorReview(comment,doctor);

                commentService.persist(comment);

                doctorReviewService.persist(doctorReview);
            } else {
                throw new RepeatedCommentException("You can add only one comment to Doctor. So you have to update or delete old one.");
            }
            return new ResponseEntity<>(comment.getContent(), HttpStatus.OK);
        }
    }
}
