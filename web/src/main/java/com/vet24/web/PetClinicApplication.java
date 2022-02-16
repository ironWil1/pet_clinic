package com.vet24.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EntityScan({"com.vet24.models"})
@EnableJpaRepositories("com.vet24.dao")
@SpringBootApplication(scanBasePackages = "com.vet24")
@EnableScheduling
public class PetClinicApplication{

    public static void main(String[] args) {
        SpringApplication.run(PetClinicApplication.class, args);
    }
}
