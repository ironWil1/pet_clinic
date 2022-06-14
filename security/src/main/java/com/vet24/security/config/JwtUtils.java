package com.vet24.security.config;

import java.sql.SQLException;
import java.util.Date;


import com.vet24.models.secutity.JwtToken;
import com.vet24.service.security.JwtTokenService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class JwtUtils {

    @Value("${pet.jwtSecret}")
    private String jwtSecret;

    @Value("${pet.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Autowired
    JwtTokenService jwtTokenService;

    public String generateJwtToken(String login) {
        String token = Jwts.builder()
                .setSubject(login)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        if (!jwtTokenService.isExistByKey(token)) {
            jwtTokenService.persist(new JwtToken(token));
        }
        return token;

    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        if (jwtTokenService.isExistByKey(authToken)) {
            try {
                Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
                return true;
            } catch (JwtException e) {
                log.info("token for authorization is not found");
            }
        }
        return false;
    }

    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            String str = "Bearer ";
            return headerAuth.substring(str.length());
        }

        return null;
    }
}