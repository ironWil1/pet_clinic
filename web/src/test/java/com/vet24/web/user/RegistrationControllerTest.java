package com.vet24.web.user;


import com.vet24.models.dto.user.RegisterDto;
import com.vet24.web.ControllerAbstractIntegrationTest;
import com.vet24.web.controllers.user.RegistrationController;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class RegistrationControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    RegistrationController registrationController;

    final String URI = "http://localhost:8090/api/registration";
    //test controller exist
    @Test
    public void getRegistrationController() {
        assertThat(registrationController).isNotNull();
    }
    //POST new client through registration
    @Test
    public void testPostNewClient(){

        RegisterDto registerDto = new RegisterDto("342354234.com","Vera","P",
                "Congo","Congo");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<RegisterDto> entity = new HttpEntity<>(registerDto, headers);
        ResponseEntity<RegisterDto> responseWrong1 =  testRestTemplate
                .exchange(URI, HttpMethod.POST, entity, RegisterDto.class);
        Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, responseWrong1.getStatusCode());

        entity.getBody().setEmail("vpcat3@gmail.com");
        entity.getBody().setConfirmPassword("Congo2");
        ResponseEntity<RegisterDto> responseWrong2 =  testRestTemplate
                .exchange(URI, HttpMethod.POST, entity, RegisterDto.class);
        Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, responseWrong2.getStatusCode());


        entity.getBody().setConfirmPassword("Congo");
        ResponseEntity<RegisterDto> responseSuccess =  testRestTemplate
                .exchange(URI, HttpMethod.POST, entity, RegisterDto.class);
        Assert.assertEquals(HttpStatus.CREATED, responseSuccess.getStatusCode());

        ResponseEntity<RegisterDto> responseDuplicate =  testRestTemplate
                .exchange(URI, HttpMethod.POST, entity, RegisterDto.class);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseDuplicate.getStatusCode());

    }
}
