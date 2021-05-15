package com.vet24.web.pet.reproduction;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.vet24.dao.pet.PetDao;
import com.vet24.dao.pet.reproduction.ReproductionDao;
import com.vet24.models.dto.pet.reproduction.ReproductionDto;
import com.vet24.models.mappers.pet.reproduction.ReproductionMapper;
import com.vet24.models.pet.reproduction.Reproduction;
import com.vet24.web.ControllerAbstractIntegrationTest;
import com.vet24.web.controllers.pet.reproduction.ReproductionController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.time.LocalDate;

@DBRider
public class ReproductionControllerTest extends ControllerAbstractIntegrationTest {
    @Autowired
    ReproductionController reproductionController;
    @Autowired
    ReproductionMapper reproductionMapper;
    @Autowired
    ReproductionDao reproductionDao;
    @Autowired
    PetDao petDao;

    final String URI = "http://localhost:8090/api/client/pet";
    final HttpHeaders HEADERS = new HttpHeaders();
    Reproduction reproduction;
    ReproductionDto reproductionDto;

    @Before
    public void createNewReproductionAndReproductionDto() {
        this.reproduction = new Reproduction(2L, LocalDate.now(), LocalDate.now(), LocalDate.now(), 5, petDao.getByKey(1L));
        this.reproductionDto = this.reproductionMapper.reproductionToReproductionDto(this.reproduction);
    }

    // get reproduction by id - success
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testA1GetReproductionSuccess() {
        ReproductionDto dtoFromDao = reproductionMapper.reproductionToReproductionDto(reproductionDao.getByKey(1L));
        ResponseEntity<ReproductionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/reproduction/{id}", ReproductionDto.class, 1, 1);

        Assert.assertEquals(dtoFromDao, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // get reproduction by id -  error 404 reproduction not found
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testA2GetReproductionError404() {
        ResponseEntity<ReproductionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/reproduction/{id}", ReproductionDto.class, 1, 11);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // get reproduction by id -  error 404 pet not found
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testA3GetReproductionError404() {
        ResponseEntity<ReproductionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/reproduction/{id}", ReproductionDto.class, 11, 1);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // get reproduction by id -  error 400 reproduction not assigned to pet
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testA4GetReproductionError400() {
        ResponseEntity<ReproductionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/reproduction/{id}", ReproductionDto.class, 2, 1);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // add reproduction - success
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true,
            executeStatementsBefore = "ALTER SEQUENCE reproduction_id_seq RESTART WITH 3;")
    public void testB1AddReproductionSuccess() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto, HEADERS);
        ResponseEntity<Void> response = testRestTemplate
                .postForEntity(URI + "/{petId}/reproduction", request, Void.class, 2);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(++beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    // add reproduction - error 404 pet not found
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testB2AddReproductionError404() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto, HEADERS);
        ResponseEntity<Void> response = testRestTemplate
                .postForEntity(URI + "/{petId}/reproduction", request, Void.class, 11);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // put reproduction by id - success
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testC1PutReproductionSuccess() throws Exception {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto, HEADERS);
        ResponseEntity<Void> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, Void.class, 1, 2);
        int afterCount = reproductionDao.getAll().size();
        Reproduction afterReproduction = reproductionDao.getByKey(2L);

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(reproduction, afterReproduction);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // put reproduction by id - error 404 pet not found
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testC2PutReproductionError404() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto, HEADERS);
        ResponseEntity<Void> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, Void.class, 11, 2);
        int afterCount = reproductionDao.getAll().size();
        Reproduction afterReproduction = reproductionDao.getByKey(2L);

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertNotEquals(reproduction, afterReproduction);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // put reproduction by id - error 404 reproduction not found
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testC3PutReproductionError404() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto, HEADERS);
        ResponseEntity<Void> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, Void.class, 1, 22);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // put reproduction by id - error 400 reproduction not assigned to pet
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testC4PutReproductionError400() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto, HEADERS);
        ResponseEntity<Void> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, Void.class, 2, 1);
        int afterCount = reproductionDao.getAll().size();
        Reproduction afterReproduction = reproductionDao.getByKey(1L);

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertNotEquals(reproduction, afterReproduction);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // delete reproduction by id - success
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testD1DeleteReproductionSuccess() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<Void> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.DELETE, request, Void.class, 1, 2);
        int afterCount = reproductionDao.getAll().size();
        Reproduction afterReproduction = reproductionDao.getByKey(2L);

        Assert.assertNull(afterReproduction);
        Assert.assertEquals(--beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // delete reproduction by id - error 404 pet not found
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testD2DeleteReproductionError404() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<Void> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.DELETE, request, Void.class, 11, 2);
        int afterCount = reproductionDao.getAll().size();
        Reproduction afterReproduction = reproductionDao.getByKey(2L);

        Assert.assertNotNull(afterReproduction);
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // delete reproduction by id - error 404 reproduction not found
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testD3DeleteReproductionError404() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<Void> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.DELETE, request, Void.class, 1, 22);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // delete reproduction by id - reproduction not assigned to pet
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testD4DeleteReproductionError400() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<Void> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.DELETE, request, Void.class, 2, 1);
        int afterCount = reproductionDao.getAll().size();
        Reproduction afterReproduction = reproductionDao.getByKey(1L);

        Assert.assertNotNull(afterReproduction);
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
