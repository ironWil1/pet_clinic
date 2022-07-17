package com.vet24.discord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DiscordApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscordApplication.class, args);
    }

}
