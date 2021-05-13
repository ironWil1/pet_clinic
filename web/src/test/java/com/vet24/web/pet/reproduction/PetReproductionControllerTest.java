package com.vet24.web.pet.reproduction;

import com.vet24.models.dto.pet.reproduction.PetReproductionDto;
import com.vet24.models.mappers.pet.reproduction.PetReproductionMapper;
import com.vet24.web.ControllerAbstractIntegrationTest;
import com.vet24.web.controllers.pet.reproduction.PetReproductionController;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.time.LocalDate;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PetReproductionControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    PetReproductionController petReproductionController;

    @Autowired
    PetReproductionMapper petReproductionMapper;

    final String URI = "http://localhost:8090/api/client/pet";

    // get reproduction by id - success
    @Test
    public void testA1GetReproductionSuccess() throws Exception {
        ResponseEntity<PetReproductionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/reproduction/{id}", PetReproductionDto.class, 1, 1);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // get reproduction by id -  error 404 reproduction not found
    @Test
    public void testA2GetReproductionError404() throws Exception {
        ResponseEntity<PetReproductionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/reproduction/{id}", PetReproductionDto.class, 1, 11);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // get reproduction by id -  error 404 pet not found
    @Test
    public void testA3GetReproductionError404() throws Exception {
        ResponseEntity<PetReproductionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/reproduction/{id}", PetReproductionDto.class, 11, 1);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // get reproduction by id -  error 400 reproduction not assigned to pet
    @Test
    public void testA4GetReproductionError400() throws Exception {
        ResponseEntity<PetReproductionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/reproduction/{id}", PetReproductionDto.class, 2, 1);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // add reproduction - success
    @Test
    public void testB1AddReproductionSuccess() throws Exception
    {
        PetReproductionDto reproductionDto = new PetReproductionDto(LocalDate.now(), LocalDate.now(), LocalDate.now(), 1);
        HttpEntity<PetReproductionDto> request = new HttpEntity<>(reproductionDto, new HttpHeaders());
        ResponseEntity<Void> response = testRestTemplate
                .postForEntity(URI + "/{petId}/reproduction", request, Void.class, 1);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    // add reproduction - error 404 pet not found
    @Test
    public void testB2AddReproductionError404() throws Exception
    {
        PetReproductionDto reproductionDto = new PetReproductionDto(LocalDate.now(), LocalDate.now(), LocalDate.now(), 2);
        HttpEntity<PetReproductionDto> request = new HttpEntity<>(reproductionDto, new HttpHeaders());
        ResponseEntity<Void> response = testRestTemplate
                .postForEntity(URI + "/{petId}/reproduction", request, Void.class, 11);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // put reproduction by id - success
    @Test
    public void testC1PutReproductionSuccess() throws Exception {
        PetReproductionDto reproductionDto = new PetReproductionDto(LocalDate.now(), LocalDate.now(), LocalDate.now(), 3);
        HttpEntity<PetReproductionDto> request = new HttpEntity<>(reproductionDto, new HttpHeaders());
        ResponseEntity<Void> response =  testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, Void.class, 1, 2);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // put reproduction by id - error 404 pet not found
    @Test
    public void testC2PutReproductionError404() throws Exception {
        PetReproductionDto reproductionDto = new PetReproductionDto(LocalDate.now(), LocalDate.now(), LocalDate.now(), 4);
        HttpEntity<PetReproductionDto> request = new HttpEntity<>(reproductionDto, new HttpHeaders());
        ResponseEntity<Void> response =  testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, Void.class, 11, 2);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // put reproduction by id - error 404 reproduction not found
    @Test
    public void testC3PutReproductionError404() throws Exception {
        PetReproductionDto reproductionDto = new PetReproductionDto(LocalDate.now(), LocalDate.now(), LocalDate.now(), 5);
        HttpEntity<PetReproductionDto> request = new HttpEntity<>(reproductionDto, new HttpHeaders());
        ResponseEntity<Void> response =  testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, Void.class, 1, 22);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // put reproduction by id - error 400 reproduction not assigned to pet
    @Test
    public void testC4PutReproductionError400() throws Exception {
        PetReproductionDto reproductionDto = new PetReproductionDto(LocalDate.now(), LocalDate.now(), LocalDate.now(), 6);
        HttpEntity<PetReproductionDto> request = new HttpEntity<>(reproductionDto, new HttpHeaders());
        ResponseEntity<Void> response =  testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, Void.class, 2, 1);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // delete reproduction by id - success
    @Test
    public void testD1DeleteReproductionSuccess() throws Exception {
        HttpEntity<Void> request = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<Void> response =  testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.DELETE, request, Void.class, 1, 2);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // delete reproduction by id - error 404 pet not found
    @Test
    public void testD2DeleteReproductionError404() throws Exception {
        HttpEntity<Void> request = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<Void> response =  testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.DELETE, request, Void.class, 11, 2);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // delete reproduction by id - error 404 reproduction not found
    @Test
    public void testD3DeleteReproductionError404() throws Exception {
        HttpEntity<Void> request = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<Void> response =  testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.DELETE, request, Void.class, 1, 22);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // delete reproduction by id - reproduction not assigned to pet
    @Test
    public void testD4DeleteReproductionError400() throws Exception {
        HttpEntity<Void> request = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<Void> response =  testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.DELETE, request, Void.class, 2, 1);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
