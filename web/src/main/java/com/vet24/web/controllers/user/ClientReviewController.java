package com.vet24.web.controllers.user;

import com.vet24.models.dto.user.DoctorReviewDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.exception.RepeatedCommentException;
import com.vet24.models.mappers.user.CommentMapper;
import com.vet24.models.mappers.user.DoctorReviewMapper;
import com.vet24.models.user.Comment;
import com.vet24.models.user.DoctorReview;
import com.vet24.models.user.User;
import com.vet24.service.user.CommentService;
import com.vet24.service.user.DoctorReviewService;
import com.vet24.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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

import java.time.LocalDateTime;

import static com.vet24.models.secutity.SecurityUtil.getOptionalOfNullableSecurityUser;


@RestController
@Slf4j
@RequestMapping("/api/client/doctor")
@Tag(name = "client review controller", description = "operations with comments")
public class ClientReviewController {

    private final UserService userService;
    private final CommentService commentService;
    private final DoctorReviewService doctorReviewService;
    private final CommentMapper commentMapper;
    private final DoctorReviewMapper doctorReviewMapper;

    @Autowired
    public ClientReviewController(UserService userService, CommentService commentService,
                                  DoctorReviewService doctorReviewService, CommentMapper commentMapper, DoctorReviewMapper doctorReviewMapper) {
        this.userService = userService;
        this.commentService = commentService;
        this.doctorReviewService = doctorReviewService;
        this.commentMapper = commentMapper;
        this.doctorReviewMapper = doctorReviewMapper;
    }

    @Operation(summary = "add comment by Client for Doctor")
    @PostMapping(value = "/{doctorId}/review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created"),
            @ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    public ResponseEntity<DoctorReviewDto> persistComment(@PathVariable("doctorId") Long doctorId, String text) {
        User doctor = userService.getByKey(doctorId);
        if (doctor == null) {
            throw new NotFoundException("Doctor not found");
        } else {
            Comment comment = null;
            DoctorReview doctorReview = null;
            User currentUser = getOptionalOfNullableSecurityUser().orElseThrow(() -> new BadRequestException("User not found"));
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
            return ResponseEntity.status(201).body(doctorReviewMapper.toDto(doctorReview));
        }
    }

    @Operation(summary = "update a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "400", description = "Another client's comment")
    })
    @PutMapping(value = "/{doctorId}/review")
    public ResponseEntity<DoctorReviewDto> updateComment(@PathVariable("doctorId") Long doctorId, @RequestBody String text) {
        Long clientId = getOptionalOfNullableSecurityUser().map(User::getId).orElseThrow(() -> new BadRequestException("User not found"));

        DoctorReview doctorReview = doctorReviewService.getByDoctorAndClientId(doctorId, clientId);
        if (doctorReview == null) {
            log.info("Doctor have bad doctorId");
            throw new NotFoundException("Comment not found");
        }
        if (!(doctorReview.getComment().getUser().getId().equals(clientId))) {
            log.info("Another client's comment with id {}", doctorReview.getComment().getId());
            throw new BadRequestException("Another client's comment");
        }
        doctorReview.getComment().setContent(text);
        doctorReviewService.update(doctorReview);
        log.info("We updated comment with this id {}", doctorReview.getComment().getId());
        return ResponseEntity.ok().body(doctorReviewMapper.toDto(doctorReview));
    }

    @Operation(summary = "delete a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment delete"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "400", description = "Another client's comment")
    })
    @DeleteMapping(value = "/{doctorId}/review")
    public ResponseEntity<Void> deleteComment(@PathVariable("doctorId") Long doctorId) {
        Long clientId = getOptionalOfNullableSecurityUser().map(User::getId).orElseThrow(() -> new BadRequestException("User not found"));

        DoctorReview doctorReview = doctorReviewService.getByDoctorAndClientId(doctorId, clientId);
        if (doctorReview == null) {
            log.info("Comment not found");
            throw new NotFoundException("Comment not found");
        }
        if (!(doctorReview.getComment().getUser().getId().equals(clientId))) {
            log.info("Another client's comment with id {}", doctorReview.getComment().getId());
            throw new BadRequestException("Another client's comment");
        }
        doctorReviewService.delete(doctorReview);
        log.info("We deleted comment with this id {}", doctorReview.getComment().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "get comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful getting comment",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorReviewDto.class))),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @GetMapping(value = "/{doctorId}/review")
    public ResponseEntity<DoctorReviewDto> getComment(@PathVariable("doctorId") Long doctorId) {
        return getOptionalOfNullableSecurityUser().map(User::getId)
                .map(userId -> doctorReviewService.getByDoctorAndClientId(doctorId, userId))
                .map(doctorReviewMapper::toDto)
                .map(dto -> ResponseEntity.ok().body(dto))
                .orElseThrow(() -> {
                    log.info("Comment not found");
                    throw new NotFoundException("Comment not found");
                });
    }
}
