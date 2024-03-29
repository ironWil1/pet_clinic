package com.vet24.web.controllers.user;

import com.vet24.models.dto.media.UploadedFileDto;
import com.vet24.models.dto.user.UserDto;
import com.vet24.models.mappers.user.UserMapper;
import com.vet24.models.user.Profile;
import com.vet24.models.user.User;
import com.vet24.service.media.ResourceService;
import com.vet24.service.media.UploadService;
import com.vet24.service.user.ProfileService;
import com.vet24.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.CheckForNull;
import java.io.IOException;

import static com.vet24.models.secutity.SecurityUtil.getOptionalOfNullableSecurityUser;


@RestController
@Slf4j
@RequestMapping("api/client")
@Tag(name = "client-controller", description = "operations with current Client")
public class ClientController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UploadService uploadService;
    private final ResourceService resourceService;

    private final ProfileService profileService;

    public ClientController(UserService userService, UserMapper userMapper, UploadService uploadService,
                            ResourceService resourceService, ProfileService profileService) {
        this.userService = userService;

        this.userMapper = userMapper;
        this.uploadService = uploadService;
        this.resourceService = resourceService;
        this.profileService = profileService;
    }

    @GetMapping("")
    @Operation(summary = "get current Client with his pets")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved the Client",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
            @ApiResponse(responseCode = "404", description = "Client not found exception", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "406", description = "Client not acceptable type", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public ResponseEntity<UserDto> getCurrentClient() {
        return getOptionalOfNullableSecurityUser()
                .map(User::getUsername)
                .flatMap(userService::getUserByEmail)
                .map(userMapper::toDto)
                .map(dto -> {
                    log.info("The current client name is{}", dto);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> {
                    log.info("The current client is not found");
                    return ResponseEntity.notFound().build();
                });
    }


    @Operation(summary = "get avatar of a Client")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved the avatar"),
            @ApiResponse(responseCode = "404", description = "Client or avatar is not found")})

    @CheckForNull
    @GetMapping("/avatar")

    public ResponseEntity<byte[]> getClientAvatar() {
        return getOptionalOfNullableSecurityUser().map(User::getProfile).map(Profile::getAvatarUrl).map(url -> {
            log.info("The client with this id {} have avatar", getOptionalOfNullableSecurityUser().map(User::getId));
            return new ResponseEntity<>(resourceService.loadAsByteArray(url), addContentHeaders(url), HttpStatus.OK);
        }).orElseGet(() -> {
            log.info("The avatar for client with id {} not found", getOptionalOfNullableSecurityUser().map(User::getId));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        });
    }

    @Operation(summary = "upload avatar for a Client")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully uploaded the avatar",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UploadedFileDto.class))),
            @ApiResponse(responseCode = "404", description = "Client is not found", content = @Content)})
    @PostMapping(value = "/avatar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UploadedFileDto> persistClientAvatar(@RequestParam("file") MultipartFile file) throws IOException {

        UploadedFileDto uploadedFileDto = uploadService.store(file);
        return getOptionalOfNullableSecurityUser()
                .map(User::getProfile)
                .map(profile -> {
                    profile.setAvatarUrl(uploadedFileDto.getUrl());
                    profileService.update(profile);
                    log.info("The avatar for client with id {} was uploaded", profile.getId());
                    return new ResponseEntity<>(uploadedFileDto, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    private HttpHeaders addContentHeaders(String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", resourceService.getContentTypeByFileName(filename));
        return headers;
    }
}
