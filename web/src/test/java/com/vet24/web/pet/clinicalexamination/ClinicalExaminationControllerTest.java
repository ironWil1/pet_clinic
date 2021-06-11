package com.vet24.web.pet.clinicalexamination;


import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.vet24.dao.pet.PetDao;
import com.vet24.dao.pet.clinicalexamination.ClinicalExaminationDao;
import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.dto.pet.clinicalexamination.ClinicalExaminationDto;
import com.vet24.models.dto.pet.reproduction.ReproductionDto;
import com.vet24.models.mappers.pet.clinicalexamination.ClinicalExaminationMapper;
import com.vet24.models.pet.reproduction.Reproduction;
import com.vet24.web.ControllerAbstractIntegrationTest;
import com.vet24.web.controllers.pet.clinicalexamination.ClinicalExaminationController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

@DBRider
public class ClinicalExaminationControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    ClinicalExaminationController clinicalExaminationController;
    @Autowired
    ClinicalExaminationMapper clinicalExaminationMapper;
    @Autowired
    ClinicalExaminationDao clinicalExaminationDao;
    @Autowired
    PetDao petDao;

    final String URI = "http://localhost:8090/api/doctor/pet";
    final HttpHeaders HEADERS = new HttpHeaders();

    ClinicalExaminationDto clinicalExaminationDtoNew;
    ClinicalExaminationDto clinicalExaminationDto1;
    ClinicalExaminationDto clinicalExaminationDto3;

    @Before
    public void createNewClinicalExaminationAndClinicalExaminationDto() {
        this.clinicalExaminationDtoNew = new ClinicalExaminationDto(4L, 10, true, "text new");
        this.clinicalExaminationDto1 = new ClinicalExaminationDto(100L, 20, false, "text 1 new");
        this.clinicalExaminationDto3 = new ClinicalExaminationDto(102L, 30, true, "text 2 new");
    }

    // get examination by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testGetReproductionSuccess() {
        ClinicalExaminationDto dtoFromDao = clinicalExaminationMapper.clinicalExaminationToClinicalExaminationDto(clinicalExaminationDao.getByKey(102L));
        ResponseEntity<ClinicalExaminationDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/exam/{examinationId}", ClinicalExaminationDto.class, 102, 102);

        Assert.assertEquals(dtoFromDao, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // get reproduction by id -  error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml.yml"})
    public void testGetReproductionError404pet() {
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/reproduction/{id}", ExceptionDto.class, 33, 102);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // get reproduction by id -  error 404 reproduction not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml.yml"})
    public void testGetReproductionError404reproduction() {
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/reproduction/{id}", ExceptionDto.class, 102, 33);
        Assert.assertEquals(response.getBody(), new ExceptionDto("reproduction not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // get reproduction by id -  error 400 reproduction not assigned to pet
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testGetReproductionError400refPetReproduction() {
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/reproduction/{id}", ExceptionDto.class, 101, 102);
        Assert.assertEquals(response.getBody(), new ExceptionDto("reproduction not assigned to this pet"));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // get reproduction by id -  error 400 pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testGetClinicalExaminationError400refDoctorPet() {
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/reproduction/{id}", ExceptionDto.class, 100, 100);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not yours"));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // add reproduction - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testAddClinicalExaminationSuccess() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ClinicalExaminationDto> request = new HttpEntity<>(clinicalExaminationDtoNew, HEADERS);
        ResponseEntity<ClinicalExaminationDto> response = testRestTemplate
                .postForEntity(URI + "/{petId}/exam", request, ClinicalExaminationDto.class, 102);
        int afterCount = clinicalExaminationDao.getAll().size();

        clinicalExaminationDtoNew.setId(response.getBody().getId());
        Assert.assertEquals(++beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), clinicalExaminationDtoNew);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    // add clinical examination - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testAddClinicalExaminationError404() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto3, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .postForEntity(URI + "/{petId}/reproduction", request, ExceptionDto.class, 33);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // add reproduction - error 400 pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testAddReproductionError400() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDtoNew, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .postForEntity(URI + "/{petId}/reproduction", request, ExceptionDto.class, 100);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not yours"));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // put reproduction by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutReproductionSuccess() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto3, HEADERS);
        ResponseEntity<ReproductionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, ReproductionDto.class, 102, 102);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(reproductionDto3, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // put reproduction by id - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutReproductionError404reproduction() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto3, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, ExceptionDto.class, 33, 102);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not found"));
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // put reproduction by id - error 404 reproduction not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutReproductionError404pet() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto3, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, ExceptionDto.class, 102, 33);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(response.getBody(), new ExceptionDto("reproduction not found"));
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // put reproduction by id - error 400 reproduction not assigned to pet
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutReproductionError400refPetReproduction() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto3, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, ExceptionDto.class, 101, 102);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(response.getBody(), new ExceptionDto("reproduction not assigned to this pet"));
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // put reproduction by id - error 400 reproductionId in path and in body not equals
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutReproductionError400idInPathAndBody() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto1, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, ExceptionDto.class, 102, 102);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(response.getBody(), new ExceptionDto("reproductionId in path and in body not equals"));
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // put reproduction by id - error 400 pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutReproductionError400refClientPet() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ReproductionDto> request = new HttpEntity<>(reproductionDto1, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.PUT, request, ExceptionDto.class, 100, 100);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not yours"));
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // delete reproduction by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testDeleteReproductionSuccess() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<Void> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.DELETE, request, Void.class, 102, 102);
        int afterCount = clinicalExaminationDao.getAll().size();
        Reproduction afterReproduction = clinicalExaminationDao.getByKey(102L);

        Assert.assertNull(afterReproduction);
        Assert.assertEquals(--beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // delete reproduction by id - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testDeleteReproductionError404reproduction() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.DELETE, request, ExceptionDto.class, 33, 102);
        int afterCount = clinicalExaminationDao.getAll().size();
        Reproduction afterReproduction = clinicalExaminationDao.getByKey(101L);

        Assert.assertNotNull(afterReproduction);
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // delete reproduction by id - error 404 reproduction not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testDeleteReproductionError404pet() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.DELETE, request, ExceptionDto.class, 102, 33);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("reproduction not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // delete reproduction by id - error reproduction not assigned to pet
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testDeleteReproductionError400refPetReproduction() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.DELETE, request, ExceptionDto.class, 101, 102);
        int afterCount = clinicalExaminationDao.getAll().size();
        Reproduction afterReproduction = clinicalExaminationDao.getByKey(102L);

        Assclert.assertNotNull(afterReproduction);
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("reproduction not assigned to this pet"));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // delete reproduction by id - error pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testDeleteReproductionError400refClientPet() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/reproduction/{id}", HttpMethod.DELETE, request, ExceptionDto.class, 100, 100);
        int afterCount = clinicalExaminationDao.getAll().size();
        Reproduction afterReproduction = clinicalExaminationDao.getByKey(100L);

        Assert.assertNotNull(afterReproduction);
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not yours"));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
