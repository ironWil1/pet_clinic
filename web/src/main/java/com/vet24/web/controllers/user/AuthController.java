package com.vet24.web.controllers.user;

import com.vet24.models.exception.BadRequestException;
import com.vet24.security.config.JwtUtils;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "Auth Controller", description = "response token")
public class AuthController {
    private static final String INVALID_TOKEN_MSG = "Registration token is invalid";

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token was succesful generated"),
            @ApiResponse(responseCode = "400", description = "Something went wrong")
    })
    @PostMapping("/auth")
    public ResponseEntity<String> authenticateUser(@RequestBody AuthRequest authRequest) {
        if (authRequest == null) {
            throw new BadRequestException(INVALID_TOKEN_MSG);
        }
        return new ResponseEntity<>(jwtUtils.generateJwtToken(authRequest.getUsername()), HttpStatus.OK);
    }

}