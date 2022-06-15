package com.vet24.web.controllers.user;

import com.vet24.models.exception.BadRequestException;
import com.vet24.models.secutity.JwtToken;
import com.vet24.models.user.User;
import com.vet24.security.config.JwtUtils;
import com.vet24.service.security.JwtTokenService;
import com.vet24.service.user.UserServiceImpl;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;



@RestController
@Tag(name = "Auth Controller", description = "response token")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserServiceImpl userService;
    private final JwtTokenService jwtTokenService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                          UserServiceImpl userService, JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.jwtTokenService=jwtTokenService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token was succesful generated"),
            @ApiResponse(responseCode = "400", description = "Something went wrong")
    })
    @PostMapping("api/auth")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody AuthRequest authRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        User user = (User) userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtUtils.generateJwtToken(authRequest.getUsername());

        return new ResponseEntity<>(new AuthResponse(token, user.getRole().getName()), HttpStatus.OK);
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token removed successfully"),
            @ApiResponse(responseCode = "400", description = "Something went wrong")
    })
    @PostMapping("api/auth/logout")
    public ResponseEntity<Void> deleteToken(HttpServletRequest request) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null){
            throw new BadRequestException("Пользователь не авторизован");
        }
        String jwt = jwtUtils.parseJwt(request);
        jwtTokenService.delete(new JwtToken(jwt));
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("api/auth/token")
    public ResponseEntity<Boolean> validToken(@RequestBody String token) {
        if (token == null) {
            return new ResponseEntity<>(false, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(jwtUtils.validateJwtToken(token), HttpStatus.OK);
    }
}