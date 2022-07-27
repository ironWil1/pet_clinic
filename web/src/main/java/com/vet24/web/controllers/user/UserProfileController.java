package com.vet24.web.controllers.user;


import com.vet24.models.dto.user.ProfileDto;
import com.vet24.models.mappers.user.ProfileMapper;
import com.vet24.models.user.Profile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import static com.vet24.models.secutity.SecurityUtil.getSecurityUserOrNull;


@RestController
@Slf4j
@RequestMapping("/api/user/profile")
@Tag(name = "user-profile-controller", description = "operations with creation of new profile")
public class UserProfileController {

    private final ProfileMapper profileMapper;


    public UserProfileController(ProfileMapper profileMapper) {
        this.profileMapper = profileMapper;
    }

    @Operation(summary = "Получить профиль по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Профиль успешно получен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProfileDto.class))),
            @ApiResponse(responseCode = "404", description = "Профиль не найден")
    })
    @GetMapping()
    public ResponseEntity<ProfileDto> getUserProfile() {
        Profile profile = getSecurityUserOrNull().getProfile();
        if (profile != null) {
            ProfileDto profileDto = profileMapper.toDto(profile);
            return ResponseEntity.ok(profileDto);
        } else {
            throw new NotFoundException("Профиль не найден");
        }
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
        Profile profile = getSecurityUserOrNull().getProfile();
        if (profile != null) {
            profileMapper.updateEntity(profileDto, profile);
            return ResponseEntity.ok().build();
        } else {
            throw new NotFoundException("Профиль не найден");
        }
    }
}
