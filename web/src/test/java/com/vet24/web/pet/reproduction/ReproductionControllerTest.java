package com.vet24.web.pet.reproduction;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.vet24.dao.pet.PetDao;
import com.vet24.dao.pet.reproduction.ReproductionDao;
import com.vet24.models.dto.exception.ExceptionDto;
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
    public void testGetReproductionSuccess() {
        ReproductionDto dtoFromDao = reproductionMapper.reproductionToReproductionDto(reproductionDao.getByKey(1L));
        ResponseEntity<ReproductionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/reproduction/{id}", ReproductionDto.class, 1, 1);

        Assert.assertEquals(dtoFromDao, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // get reproduction by id -  error 404 reproduction not found
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testGetReproductionError404reproduction() {
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/reproduction/{id}", ExceptionDto.class, 1, 11);
        Assert.assertEquals(response.getBody(), new ExceptionDto("reproduction not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // get reproduction by id -  error 404 pet not found
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testGetReproductionError404pet() {
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/reproduction/{id}", ExceptionDto.class, 11, 1);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // get reproduction by id -  error 400 reproduction not assigned to pet
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testGetReproductionError400refPetReproduction() {
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/reproduction/{id}", ExceptionDto.class, 2, 1);
        Assert.assertEquals(response.getBody(), new ExceptionDto("reproduction not assigned to this pet"));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // get reproduction by id -  error 400 pet not yours
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testGetReproductionError400refClientPet() {
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/reproduction/{id}", ExceptionDto.class, 3, 3);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not yours"));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // add reproduction - success
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true,
            executeStatementsBefore = "ALTER SEQUENCE reproduction_id_seq RESTART WITH 4;")
    public void testAddReproductionSuccess() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto, HEADERS);
        ResponseEntity<ReproductionDto> response = testRestTemplate
                .postForEntity(URI + "/{petId}/reproduction", request, ReproductionDto.class, 2);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(++beforeCount, afterCount);
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    // add reproduction - error 404 pet not found
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testAddReproductionError404() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .postForEntity(URI + "/{petId}/reproduction", request, ExceptionDto.class, 11);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // add reproduction - error 400 pet not yours
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testAddReproductionError400() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .postForEntity(URI + "/{petId}/reproduction", request, ExceptionDto.class, 3);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not yours"));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // put reproduction by id - success
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testPutReproductionSuccess() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto, HEADERS);
        ResponseEntity<ReproductionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, ReproductionDto.class, 1, 2);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(reproductionDto, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // put reproduction by id - error 404 pet not found
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testPutReproductionError404reproduction() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, ExceptionDto.class, 11, 2);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not found"));
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // put reproduction by id - error 404 reproduction not found
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testPutReproductionError404pet() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, ExceptionDto.class, 1, 22);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(response.getBody(), new ExceptionDto("reproduction not found"));
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // put reproduction by id - error 400 reproduction not assigned to pet
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testPutReproductionError400refPetReproduction() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, ExceptionDto.class, 2, 1);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(response.getBody(), new ExceptionDto("reproduction not assigned to this pet"));
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // put reproduction by id - error 400 reproductionId in path and in body not equals
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testPutReproductionError400idInPathAndBody() {
        ReproductionDto newDto = reproductionMapper.reproductionToReproductionDto(reproduction);
        newDto.setId(3L);

        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(newDto, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, ExceptionDto.class, 1, 2);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(response.getBody(), new ExceptionDto("reproductionId in path and in body not equals"));
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // put reproduction by id - error 400 pet not yours
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testPutReproductionError400refClientPet() {
        ReproductionDto newDto = reproductionMapper.reproductionToReproductionDto(reproduction);
        newDto.setId(3L);

        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(newDto, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, ExceptionDto.class, 3, 3);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not yours"));
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // delete reproduction by id - success
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testDeleteReproductionSuccess() {
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
    public void testDeleteReproductionError404reproduction() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.DELETE, request, ExceptionDto.class, 11, 2);
        int afterCount = reproductionDao.getAll().size();
        Reproduction afterReproduction = reproductionDao.getByKey(2L);

        Assert.assertNotNull(afterReproduction);
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // delete reproduction by id - error 404 reproduction not found
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testDeleteReproductionError404pet() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.DELETE, request, ExceptionDto.class, 1, 22);
        int afterCount = reproductionDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("reproduction not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // delete reproduction by id - error reproduction not assigned to pet
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testDeleteReproductionError400refPetReproduction() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.DELETE, request, ExceptionDto.class, 2, 1);
        int afterCount = reproductionDao.getAll().size();
        Reproduction afterReproduction = reproductionDao.getByKey(1L);

        Assert.assertNotNull(afterReproduction);
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("reproduction not assigned to this pet"));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // delete reproduction by id - error pet not yours
    @Test
    @DataSet(value = "/datasets/reproduction.yml", cleanBefore = true, disableConstraints = true)
    public void testDeleteReproductionError400refClientPet() {
        int beforeCount = reproductionDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.DELETE, request, ExceptionDto.class, 3, 3);
        int afterCount = reproductionDao.getAll().size();
        Reproduction afterReproduction = reproductionDao.getByKey(3L);

        Assert.assertNotNull(afterReproduction);
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not yours"));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
