package com.vet24.web.controllers.user;


import com.vet24.models.dto.user.ProfileDto;
import com.vet24.models.mappers.user.ProfileMapper;
import com.vet24.models.user.User;
import com.vet24.service.user.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import static com.vet24.models.secutity.SecurityUtil.getOptionalOfNullableSecurityUser;


@RestController
@Slf4j
@RequestMapping("/api/user/profile")
@Tag(name = "user-profile-controller", description = "operations with creation of new profile")
public class UserProfileController {

    private final ProfileMapper profileMapper;

    private final ProfileService profileService;


    public UserProfileController(ProfileMapper profileMapper, ProfileService profileService) {
        this.profileMapper = profileMapper;
        this.profileService = profileService;
    }

    @Operation(summary = "Получить профиль")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Профиль успешно получен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProfileDto.class))),
            @ApiResponse(responseCode = "404", description = "Профиль не найден")
    })
    @GetMapping()
    public ResponseEntity<ProfileDto> getUserProfile() {
        return getOptionalOfNullableSecurityUser().map(User::getProfile)
                .map(profileMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Профиль не найден"));
    }

    @Operation(summary = "Обновление профиля")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Профиль обновлен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProfileDto.class))),
            @ApiResponse(responseCode = "400", description = "Профиль не найден")
    })
    @PutMapping()
    public ResponseEntity<Void> updateProfile(@RequestBody ProfileDto profileDto) {
        return getOptionalOfNullableSecurityUser().map(User::getProfile)
                .map(profile -> {
                    profileMapper.updateEntity(profileDto, profile);
                    return profileService.update(profile);
                }).map(x -> new ResponseEntity<Void>(HttpStatus.OK))
                .orElseThrow(() -> new NotFoundException("Профиль не найден"));
    }
}
