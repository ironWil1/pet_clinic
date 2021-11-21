package com.vet24.web.controllers.user;

import com.vet24.models.exception.BadRequestException;
import com.vet24.security.config.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {
    private static final String INVALID_TOKEN_MSG = "Registration token is invalid";

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;


    @PostMapping("/auth")
    public ResponseEntity<String> authenticateUser(@RequestBody AuthRequest authRequest) {
        if(authRequest == null) {
            throw new BadRequestException(INVALID_TOKEN_MSG);
        }
        return new ResponseEntity<>(jwtUtils.generateJwtToken(authRequest.getUsername()), HttpStatus.OK);

    }

}