package com.vet24.web.controllers.user;

import com.vet24.models.dto.user.DoctorReviewDto;
import com.vet24.models.mappers.user.DoctorReviewMapper;
import com.vet24.service.user.DoctorReviewService;
import com.vet24.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/user/doctor")
@Tag(name = "user doctor review сontroller", description = "operations with doctor reviews")
public class UserDoctorReviewController {
    private final UserService userService;
    private final DoctorReviewService doctorReviewService;
    private final DoctorReviewMapper doctorReviewMapper;

    @Autowired
    public UserDoctorReviewController(UserService userService, DoctorReviewMapper doctorReviewMapper,
                                      DoctorReviewService doctorReviewService) {
        this.userService = userService;
        this.doctorReviewMapper = doctorReviewMapper;
        this.doctorReviewService = doctorReviewService;
    }

    @Operation(summary = "find all reviews")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews found"),
            @ApiResponse(responseCode = "404", description = "Doctor not found"),
    })

    @GetMapping(value = "/{doctorId}/review")
    public ResponseEntity<List<DoctorReviewDto>> getAllReviewByDoctorId(@PathVariable("doctorId") Long doctorId) {
        if (!userService.isExistByKey(doctorId)) {
            throw new NotFoundException("Doctor not found");
        }
        List<DoctorReviewDto> doctorReviewDto = doctorReviewMapper.toDto(doctorReviewService.getAllReviewByDoctorId(doctorId));
        return new ResponseEntity<>(doctorReviewDto, HttpStatus.OK);
    }
}

