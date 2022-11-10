package com.vet24.web.controllers.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.user.*;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.exception.RepeatedRegistrationException;
import com.vet24.models.mappers.user.AdminUserMapper;
import com.vet24.models.mappers.user.ProfileMapper;
import com.vet24.models.user.Profile;
import com.vet24.models.user.User;
import com.vet24.models.util.View;
import com.vet24.service.media.MailService;
import com.vet24.service.user.ProfileService;
import com.vet24.service.user.RoleService;
import com.vet24.service.user.UserService;
import com.vet24.service.user.VerificationService;
import com.vet24.web.controllers.annotations.CheckExist;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/admin/user")
@Tag(name = "AdminUser controller", description = "CRUD операции с пользователями")
public class AdminUserController {

    private final UserService userService;
    private final AdminUserMapper adminUserMapper;
    private final VerificationService verificationService;
    private final MailService mailService;
    private final ProfileService profileService;
    private final ProfileMapper profileMapper;
    private final RoleService roleService;
    @Value("${registration.repeated.error.msg}")
    private String repeatedRegistrationMsg;

    public AdminUserController(UserService userService, AdminUserMapper adminUserMapper,
                               VerificationService verificationService, MailService mailService,
                               ProfileMapper profileMapper, ProfileService profileService, RoleService roleService) {
        this.userService = userService;
        this.adminUserMapper = adminUserMapper;
        this.verificationService = verificationService;
        this.mailService = mailService;
        this.profileMapper = profileMapper;
        this.profileService = profileService;
        this.roleService = roleService;
    }

    @Operation(summary = "Получить список пользователей")
    @ApiResponse(responseCode = "200", description = "Список выдан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)))
    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getAll() {
        List<User> userList = userService.getAll();
        return new ResponseEntity<>(adminUserMapper.toDto(userList), HttpStatus.OK);
    }

    @Operation(summary = "Получить пользователя по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список выдан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Пользователь с таким id не найден"),
    })
    @GetMapping("{id}")
    public ResponseEntity<UserResponseDto> getById(@CheckExist(entityClass = User.class) @PathVariable Long id) {
        User user = userService.getByKey(id);
        return new ResponseEntity<>(adminUserMapper.toDto(user), HttpStatus.OK);
    }


    @Operation(summary = "Создать нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь создан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRequestDto.class))),
            @ApiResponse(responseCode = "400", description = "Тело запроса не удовлетворяет требованиям"),
    })
    @PostMapping()
    public ResponseEntity<UserResponseDto> save(@JsonView(View.Post.class) @Valid @RequestBody UserRequestDto userRequestDto) {
        Optional<User> foundOrNew = userService.getUserByEmail(userRequestDto.getEmail());
        if (foundOrNew.isPresent()) {
            log.info("User with an email {} already exists", foundOrNew.get().getEmail());
            throw new RepeatedRegistrationException(repeatedRegistrationMsg);
        }

        User user = adminUserMapper.toEntity(userRequestDto);

        if (userRequestDto.getRole().equals(RoleNameEnum.CLIENT)) {
            user.setRole(roleService.getByKey(RoleNameEnum.UNVERIFIED_CLIENT));

            String tokenUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString() +
                    "/api/registration/confirm" + verificationService.createVerificationToken(user);
            mailService.sendWelcomeMessage(userRequestDto.getEmail(), userRequestDto.getFirstName(), tokenUrl);
            log.info("The registration for client with id {} is created ", user.getId());
        } else {
            user.setRole(roleService.getByKey(userRequestDto.getRole()));
        }

        Profile profile = profileMapper.toEntity(userRequestDto);
        profile.setUser(user);

        userService.persist(user);
        profileService.persist(profile);

        return new ResponseEntity<>(adminUserMapper.toDto(user), HttpStatus.CREATED);
    }


    @Operation(summary = "Обновить данные пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные пользователя обновлены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRequestDto.class))),
            @ApiResponse(responseCode = "400", description = "Пользователь с таким id не найден"),
    })
    @PutMapping("{id}")
    public ResponseEntity<UserResponseDto> update(@JsonView(View.Put.class) @Valid @RequestBody UserRequestDto userRequestDto,
                                                @CheckExist(entityClass = User.class) @PathVariable("id") long id) {
        User user = userService.getByKey(id);
        user.setRole(roleService.getByKey(userRequestDto.getRole()));
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());

        Profile profile = profileService.getByKey(user.getProfile().getId());
        profileMapper.updateEntity(userRequestDto, profile);

        user.setProfile(profile);

        userService.update(user);
        profileService.update(profile);

        return new ResponseEntity<>(adminUserMapper.toDto(user), HttpStatus.OK);
    }

    //TODO после фикса каскадов
//    @Operation(summary = "Удалить пользователя")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Пользователь удален",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = UserRequestDto.class))),
//            @ApiResponse(responseCode = "400", description = "Пользователь с таким id не найден"),
//    })
//    @DeleteMapping("{id}")
//    public ResponseEntity<Void> delete(@CheckExist(entityClass = User.class) @PathVariable("id") long id) {
//        User user = userService.getByKey(id);
//        userService.delete(user);
//        return ResponseEntity.ok();
//    }
}
