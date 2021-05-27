package com.vet24.web.user;


import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.vet24.models.dto.user.RegisterDto;
import com.vet24.web.ControllerAbstractIntegrationTest;
import com.vet24.web.controllers.user.RegistrationController;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

@Slf4j
@DBRider
public class RegistrationControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    RegistrationController registrationController;

    final String URI = "http://localhost:8090/api/registration";

    @Test
    @DataSet(value = "/datasets/registration.yml", cleanBefore = true)
    public void shouldBeNotAcceptableWrongEmail() {
        RegisterDto registerDto = new RegisterDto("342354234.com","Vera","P",
                "Congo","Congo");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<RegisterDto> entity = new HttpEntity<>(registerDto, headers);
        ResponseEntity<RegisterDto> responseEntity =  testRestTemplate
                .exchange(URI, HttpMethod.POST, entity, RegisterDto.class);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DataSet(value = "/datasets/registration.yml", cleanBefore = true)
    public void shouldBeNotAcceptablePasswords(){
        RegisterDto registerDto = new RegisterDto("342354234@com","Vera","P",
                "Congo","Congo2");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<RegisterDto> entity = new HttpEntity<>(registerDto, headers);
        ResponseEntity<RegisterDto> responseEntity =  testRestTemplate
                .exchange(URI, HttpMethod.POST, entity, RegisterDto.class);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DataSet(value = "/datasets/registration.yml", cleanBefore = true)
    public void shouldBeCreated()  {
        RegisterDto registerDto = new RegisterDto("342354234@gmail.com","Vera","P",
                "Congo","Congo");

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<RegisterDto> entity = new HttpEntity<>(registerDto, headers);
        ResponseEntity<RegisterDto> responseEntity =  testRestTemplate
                .exchange(URI, HttpMethod.POST, entity, RegisterDto.class);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }
}
