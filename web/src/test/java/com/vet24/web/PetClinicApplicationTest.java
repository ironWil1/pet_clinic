package com.vet24.web;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.AnnotationConfigContextLoader;


@SpringBootTest
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("test")
@TestPropertySource("classpath:/application-test.properties")
public abstract class PetClinicApplicationTest {

    @org.springframework.context.annotation.Configuration
    public static class ContextConfiguration {
    }
}
