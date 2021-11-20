package com.vet24.web.controllers.user;

import com.vet24.security.config.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;


    @PostMapping("/auth")
    public ResponseEntity authenticateUser(@RequestBody AuthRequest authRequest) {
        if (!authRequest.equals(null)) {
        return new ResponseEntity(jwtUtils.generateJwtToken(authRequest.getUsername()), HttpStatus.OK);
        } else {
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}