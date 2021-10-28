package com.vet24.web.controllers.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/admin/comment/")
@Tag(name = "admin comment controller", description = "adminCommentController operations")
@Slf4j
public class AdminDoctorScheduleController {
}
