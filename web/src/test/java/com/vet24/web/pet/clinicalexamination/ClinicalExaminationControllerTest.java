package com.vet24.web.pet.clinicalexamination;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.pet.PetDao;
import com.vet24.dao.pet.clinicalexamination.ClinicalExaminationDao;
import com.vet24.dao.user.DoctorDao;
import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.dto.pet.clinicalexamination.ClinicalExaminationDto;
import com.vet24.models.mappers.pet.clinicalexamination.ClinicalExaminationMapper;
import com.vet24.models.pet.clinicalexamination.ClinicalExamination;
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

import java.time.LocalDate;
import java.util.List;
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
    ClinicalExaminationDto clinicalExaminationDtoNew;
    ClinicalExaminationDto clinicalExaminationDto1;
    ClinicalExaminationDto clinicalExaminationDto3;

    @Before
    public void createNewClinicalExaminationAndClinicalExaminationDto() {
        this.clinicalExaminationDtoNew = new ClinicalExaminationDto(100L, 100L, 10.0, true, "textNew");
        this.clinicalExaminationDto1 = new ClinicalExaminationDto(101L, 101L, 20.0, true, "text1");
        this.clinicalExaminationDto3 = new ClinicalExaminationDto(102L, 102L, 40.0, true, "text3");
    }

    // get clinical examination by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testGetClinicalExaminationSuccess() {
        ClinicalExaminationDto dtoFromDao = clinicalExaminationMapper
                .toDto(clinicalExaminationDao.getByKey(102L));
        ResponseEntity<ClinicalExaminationDto> response = testRestTemplate
                .getForEntity(URI + "/{examinationId}", ClinicalExaminationDto.class, 102, 102);

        Assert.assertEquals(dtoFromDao, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // GET ClinicalExamination by id - 404 ERROR "ClinicalExamination not found"
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testGetClinicalExaminationErrorClinicalExaminationNotFound() {
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .getForEntity(URI + "/10806", ExceptionDto.class, 102, 33);

        Assert.assertEquals(response.getBody(), new ExceptionDto("clinical examination not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // add clinical examination - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testAddClinicalExaminationSuccess() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ClinicalExaminationDto> request = new HttpEntity<>(clinicalExaminationDtoNew, HEADERS);
        ResponseEntity<ClinicalExaminationDto> response = testRestTemplate
                .postForEntity(URI + "", request, ClinicalExaminationDto.class, 102, 102);
        int afterCount = clinicalExaminationDao.getAll().size();
        clinicalExaminationDtoNew.setId(Objects.requireNonNull(response.getBody()).getId());

        Assert.assertEquals(++beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), clinicalExaminationDtoNew);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    // put clinical examination by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutClinicalExaminationSuccess() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ClinicalExaminationDto> request = new HttpEntity<>(clinicalExaminationDto3, HEADERS);
        ResponseEntity<ClinicalExaminationDto> response = testRestTemplate
                .exchange(URI + "/{examinationId}", HttpMethod.PUT, request, ClinicalExaminationDto.class, 102, 102);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(clinicalExaminationDto3, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // delete clinical examination by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testDeleteClinicalExaminationSuccess() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/100", HttpMethod.DELETE, request, ExceptionDto.class, 33);
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
