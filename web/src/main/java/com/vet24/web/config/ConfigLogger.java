package com.vet24.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

@Configuration
public class ConfigLogger {

    @Bean
    public Logger getLog(){
        return Logger.getLogger("com.vet24.web");
    }
}
