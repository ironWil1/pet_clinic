package com.vet24.web.pet.clinicalexamination;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.pet.clinicalexamination.ClinicalExaminationDao;
import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.dto.pet.clinicalexamination.ClinicalExaminationDto;
import com.vet24.models.mappers.pet.clinicalexamination.ClinicalExaminationMapper;
import com.vet24.web.ControllerAbstractIntegrationTest;
import com.vet24.web.controllers.pet.clinicalexamination.ClinicalExaminationController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Objects;

@WithUserDetails(value = "doctor103@email.com")
public class ClinicalExaminationControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    ClinicalExaminationController clinicalExaminationController;

    @Autowired
    ClinicalExaminationDao clinicalExaminationDao;

    @Autowired
    ClinicalExaminationMapper clinicalExaminationMapper;

    final String URI = "http://localhost:8090/api/doctor/exam";
    final HttpHeaders HEADERS = new HttpHeaders();
    ClinicalExaminationDto clinicalExaminationDtoNew1;
    ClinicalExaminationDto clinicalExaminationDtoNew2;
    ClinicalExaminationDto clinicalExaminationDto1;
    ClinicalExaminationDto clinicalExaminationDto2;
    ClinicalExaminationDto clinicalExaminationDto3;
    ClinicalExaminationDto clinicalExaminationDto4;
    ClinicalExaminationDto clinicalExaminationDto5;

    @Before
    public void createNewClinicalExaminationAndClinicalExaminationDto() {
        this.clinicalExaminationDtoNew1 = new ClinicalExaminationDto(100L, 100L, 10.0, true, "textNew");
        this.clinicalExaminationDtoNew2 = new ClinicalExaminationDto(100L, 107670L, 10.0, true, "textNew");
        this.clinicalExaminationDto1 = new ClinicalExaminationDto(101L, 101L, 20.0, true, "text1");
        this.clinicalExaminationDto2 = new ClinicalExaminationDto(1545452L, 102L, 40.0, true, "text3");
        this.clinicalExaminationDto3 = new ClinicalExaminationDto(102L, 102L, 40.0, true, "text3");
        this.clinicalExaminationDto4 = new ClinicalExaminationDto(101L, 1043432L, 40.0, true, "text3");
        this.clinicalExaminationDto5 = new ClinicalExaminationDto(101L, 106L, 40.0, true, "text3");
    }

    // get clinical examination by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testGetClinicalExaminationSuccess() {
        ClinicalExaminationDto dtoFromDao = clinicalExaminationMapper
                .toDto(clinicalExaminationDao.getByKey(102L));
        ResponseEntity<ClinicalExaminationDto> response = testRestTemplate
                .getForEntity(URI + "/{examinationId}", ClinicalExaminationDto.class, 102);

        Assert.assertEquals(dtoFromDao, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // get ClinicalExamination by id - ClinicalExamination not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testGetClinicalExaminationErrorClinicalExaminationNotFound() {
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .getForEntity(URI + "/{examinationId}", ExceptionDto.class, 100856);

        Assert.assertEquals(response.getBody(), new ExceptionDto("clinical examination not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // add ClinicalExamination - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testAddClinicalExaminationSuccess() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ClinicalExaminationDto> request = new HttpEntity<>(clinicalExaminationDtoNew1, HEADERS);
        ResponseEntity<ClinicalExaminationDto> response = testRestTemplate
                .postForEntity(URI, request, ClinicalExaminationDto.class);
        int afterCount = clinicalExaminationDao.getAll().size();
        clinicalExaminationDtoNew1.setId(Objects.requireNonNull(response.getBody()).getId());

        Assert.assertEquals(++beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), clinicalExaminationDtoNew1);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    // add ClinicalExamination - pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testAddClinicalExaminationErrorPetNotFound() {
        HttpEntity<ClinicalExaminationDto> request = new HttpEntity<>(clinicalExaminationDtoNew2, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .postForEntity(URI, request, ExceptionDto.class);

        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // put clinical examination by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutClinicalExaminationSuccess() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ClinicalExaminationDto> request = new HttpEntity<>(clinicalExaminationDto3, HEADERS);
        ResponseEntity<ClinicalExaminationDto> response = testRestTemplate
                .exchange(URI + "/{examinationId}", HttpMethod.PUT, request, ClinicalExaminationDto.class, 102);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(clinicalExaminationDto3, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // put clinical examination by id - PetNotAssigned
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutClinicalExaminationErrorPetNotAssigned() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ClinicalExaminationDto> request = new HttpEntity<>(clinicalExaminationDto5, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{examinationId}", HttpMethod.PUT, request, ExceptionDto.class, 102);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(new ExceptionDto("clinical examination not assigned to this pet"), response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // put clinical examination by id - BadRequest
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutClinicalExaminationErrorBadRequest() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ClinicalExaminationDto> request = new HttpEntity<>(clinicalExaminationDto2, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{examinationId}", HttpMethod.PUT, request, ExceptionDto.class, 102);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(new ExceptionDto("examinationId in path and in body not equals"), response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // put clinical examination by id - pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutClinicalExaminationErrorPetNotFound() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ClinicalExaminationDto> request = new HttpEntity<>(clinicalExaminationDto4, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{examinationId}", HttpMethod.PUT, request, ExceptionDto.class, 102);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(new ExceptionDto("pet not found"), response.getBody());
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // put clinical examination by id - Not Found clinical examination
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutClinicalExaminationErrorClinicalExaminationNotFound() {
        HttpEntity<ClinicalExaminationDto> request = new HttpEntity<>(clinicalExaminationDto3, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{examinationId}", HttpMethod.PUT, request, ExceptionDto.class, 108562);

        Assert.assertEquals(new ExceptionDto("clinical examination not found"), response.getBody());
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // delete clinical examination by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testDeleteClinicalExaminationSuccess() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{examinationId}", HttpMethod.DELETE, request, ExceptionDto.class, 100);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(beforeCount, ++afterCount);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // delete clinical examination by id - clinical examination not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testDeleteClinicalExaminationErrorNotFound() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{examinationId}", HttpMethod.DELETE, request, ExceptionDto.class, 33);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("clinical examination not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
