package com.vet24.web.config;


import com.vet24.security.config.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class TokenCreator {

//    @Autowired
//    JwtUtils jwtUtils;
    private JwtUtils jwtUtils;
    public static String jwtClient;

    public TokenCreator() {
        jwtUtils = new JwtUtils();
        jwtClient = createClientToken();
    }

    public String createClientToken() {

        return jwtUtils.generateJwtToken("client1@email.com");
    }

}
