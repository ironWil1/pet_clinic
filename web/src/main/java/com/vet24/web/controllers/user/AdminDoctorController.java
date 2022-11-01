package com.vet24.web.controllers.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.user.DoctorDtoPost;
import com.vet24.models.dto.user.UserDoctorDto;
import com.vet24.models.dto.user.UserDto;
import com.vet24.models.mappers.user.DoctorPostMapper;
import com.vet24.models.mappers.user.UserMapper;
import com.vet24.models.user.User;
import com.vet24.models.util.View;
import com.vet24.service.user.UserService;
import com.vet24.web.controllers.annotations.CheckExist;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/admin/user/doctor")
@Tag(name = "AdminDoctor controller", description = "CRUD operations")
public class AdminDoctorController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final DoctorPostMapper doctorPostMapper;


    public AdminDoctorController(UserService userService, UserMapper userMapper, DoctorPostMapper doctorPostMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.doctorPostMapper = doctorPostMapper;
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getDoctorById(@CheckExist (entityClass = User.class) @PathVariable Long id) {
        User doctor = userService.getByKey(id);
            UserDto doctorDto = userMapper.toDto(doctor);
            return ResponseEntity.ok(doctorDto);
    }

    @GetMapping("")
    public ResponseEntity<List<UserDto>> getAllDoctors() {
        List<User> doctorList = userService.getAll();
        return ResponseEntity.ok(userMapper.toDto(doctorList));
    }


    @Operation(summary = "Create new doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor is create",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDtoPost.class))),
            @ApiResponse(responseCode = "404", description = "Doctor not found"),
    })
    @PostMapping
    public ResponseEntity<UserDoctorDto> doctorDtoPost(@JsonView(View.Post.class) @Valid @RequestBody DoctorDtoPost doctorDtoPost) {
        User doctor = doctorPostMapper.toEntity(doctorDtoPost);
        userService.persist(doctor);
        return ResponseEntity.status(200).body(doctorPostMapper.toDto(doctor));
    }

    @Operation(summary = "Update doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor is update",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDtoPost.class))),
            @ApiResponse(responseCode = "400", description = "Doctor not found"),
    })
    @PutMapping("{id}")
    public ResponseEntity<UserDto> doctorDtoPut(@JsonView(View.Put.class) @Valid @RequestBody DoctorDtoPost doctorDtoPost,
                                                @CheckExist(entityClass = User.class) @PathVariable("id") long id) {
        User doctor = userService.getByKey(id);

        doctor.setPassword(doctorDtoPost.getPassword());

        userService.update(doctor);
        return ResponseEntity.ok(userMapper.toDto(doctor));
    }

    @Operation(summary = "Delete doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor removed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDtoPost.class))),
            @ApiResponse(responseCode = "400", description = "Doctor not found"),
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteDto(@CheckExist(entityClass = User.class) @PathVariable("id") long id) {
        User doctor = userService.getByKey(id);
            userService.delete(doctor);
            return ResponseEntity.ok().build();
    }

}