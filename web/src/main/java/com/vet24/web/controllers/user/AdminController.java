package com.vet24.web.controllers.user;

import com.vet24.models.user.Admin;
import com.vet24.service.media.ResourceService;
import com.vet24.service.media.UploadService;
import com.vet24.service.user.AdminService;
import com.vet24.service.user.CommentService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin")
@Slf4j
@Tag(name = "admin-controller", description = "operations with current admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the Admin",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Admin.class)))
    })
    public ResponseEntity<Admin> getAdmin() {

        Admin admin = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (admin != null) {
            log.info(admin.getLastname());
        } else
            log.info("admin not found");

        return admin != null ? ResponseEntity.ok(adminService.getAdminByEmail(admin.getEmail())) : ResponseEntity.notFound().build();
    }
}
