package com.vet24.web.pet.clinicalexamination;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
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
import org.springframework.http.*;

import java.util.Objects;

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
    @Autowired
    DoctorDao doctorDao;

    final String URI = "http://localhost:8090/api/doctor/pet";
    final HttpHeaders HEADERS = new HttpHeaders();

    ClinicalExaminationDto clinicalExaminationDtoNew;
    ClinicalExaminationDto clinicalExaminationDto1;
    ClinicalExaminationDto clinicalExaminationDto3;

    // clinical examination 4 --> pet 1 --> examination 1 (to check doctor-pet link)
    // client 3 --> pet 2 --> clinical examination 2 (to check pet-clinical examination link)
    //        `---> pet 3 --> clinical examination 3 (to get & update & delete)
    //                  `---> clinical examination 4 (to create)

    @Before
    public void createNewClinicalExaminationAndClinicalExaminationDto() {
        this.clinicalExaminationDtoNew = new ClinicalExaminationDto(4L, 10, true, "textNew");
        this.clinicalExaminationDto1 = new ClinicalExaminationDto(100L, 20, true, "text1");
        this.clinicalExaminationDto3 = new ClinicalExaminationDto(102L, 40, true, "text3");
    }

    // + 1. get clinical examination by id - success (pet id 102 and examination id 102 found in db test_pets)
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testGetClinicalExaminationSuccess() {
        ClinicalExaminationDto dtoFromDao = clinicalExaminationMapper.clinicalExaminationToClinicalExaminationDto(clinicalExaminationDao.getByKey(102L));
        ResponseEntity<ClinicalExaminationDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/exam/{examinationId}", ClinicalExaminationDto.class, 102, 102);

        Assert.assertEquals(dtoFromDao, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // + 2. get clinical examination by id -  error 404 (pet id 33 not found)
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testGetClinicalExaminationError404pet() {
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/exam/{examinationId}", ExceptionDto.class, 33, 102);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // + 3. get clinical examination by id -  error 404 clinical examination not found (id 33 clinical examination not found)
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testGetClinicalExaminationError404examination() {
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/exam/{examinationId}", ExceptionDto.class, 102, 33);
        Assert.assertEquals(response.getBody(), new ExceptionDto("clinical examination not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // + 4. get clinical examination by id -  error 400 (clinical examination not assigned to pet)
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testGetClinicalExaminationError400refPetExamination() {
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/exam/{examinationId}", ExceptionDto.class, 101, 102);
        Assert.assertEquals(response.getBody(), new ExceptionDto("clinical examination not assigned to this pet"));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // - 5. get clinical examination by id -  error 400 (the pet is not assigned a doctor)
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testGetClinicalExaminationError400refDoctorPet() {
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/exam/{examinationId}", ExceptionDto.class, 100, 100);
        Assert.assertEquals(response.getBody(), new ExceptionDto("the pet is not assigned a doctor"));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // + add clinical examination - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testAddRClinicalExaminationSuccess() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ClinicalExaminationDto> request = new HttpEntity<>(clinicalExaminationDtoNew, HEADERS);
        ResponseEntity<ClinicalExaminationDto> response = testRestTemplate
                .postForEntity(URI + "/{petId}/exam", request, ClinicalExaminationDto.class, 102);
        int afterCount = clinicalExaminationDao.getAll().size();

        clinicalExaminationDtoNew.setId(Objects.requireNonNull(response.getBody()).getId());
        Assert.assertEquals(++beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), clinicalExaminationDtoNew);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    // - add clinical examination - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testAddClinicalExaminationError404() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ClinicalExaminationDto> request = new HttpEntity<>(clinicalExaminationDto3, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .postForEntity(URI + "/{petId}/exam", request, ExceptionDto.class, 33);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // - add clinical examination - error 400 pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testAddClinicalExaminationError400() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ClinicalExaminationDto> request = new HttpEntity<>(clinicalExaminationDtoNew, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .postForEntity(URI + "/{petId}/exam", request, ExceptionDto.class, 100);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not yours"));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // + put clinical examination by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutClinicalExaminationSuccess() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ClinicalExaminationDto> request = new HttpEntity<>(clinicalExaminationDto3, HEADERS);
        ResponseEntity<ClinicalExaminationDto> response = testRestTemplate
                .exchange(URI + "/{petId}/exam/{examinationId}", HttpMethod.PUT, request, ClinicalExaminationDto.class, 102, 102);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(clinicalExaminationDto3, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // - put examinationId by id - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutClinicalExaminationError404examination() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ClinicalExaminationDto> request = new HttpEntity<>(clinicalExaminationDto3, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/exam/{examinationId}", HttpMethod.PUT, request, ExceptionDto.class, 33, 102);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not found"));
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // - put clinical examination by id - error 404 clinical examination not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutClinicalExaminationError404pet() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ClinicalExaminationDto> request = new HttpEntity<>(clinicalExaminationDto3, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/exam/{examinationId}", HttpMethod.PUT, request, ExceptionDto.class, 102, 33);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(response.getBody(), new ExceptionDto("clinical examination not found"));
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // + put clinical examination by id - error 400 clinical examination not assigned to pet
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutClinicalExaminationError400refPetExamination() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ClinicalExaminationDto> request = new HttpEntity<>(clinicalExaminationDto3, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/exam/{examinationId}", HttpMethod.PUT, request, ExceptionDto.class, 101, 102);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(response.getBody(), new ExceptionDto("clinical examination not assigned to this pet"));
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // + put clinical examination by id - error 400 clinical examination in path and in body not equals
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutClinicalExaminationError400idInPathAndBody() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ClinicalExaminationDto> request = new HttpEntity<>(clinicalExaminationDto1, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/exam/{examinationId}", HttpMethod.PUT, request, ExceptionDto.class, 102, 102);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(response.getBody(), new ExceptionDto("examinationId in path and in body not equals"));
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // - put clinical examination by id - error 400 pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testPutClinicalExaminationError400refClientPet() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<ClinicalExaminationDto> request = new HttpEntity<>(clinicalExaminationDto1, HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/exam/{examinationId}", HttpMethod.PUT, request, ExceptionDto.class, 100, 100);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not yours"));
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // - delete clinical examination by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testDeleteClinicalExaminationSuccess() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<Void> response = testRestTemplate
                .exchange(URI + "/{petId}/exam/{examinationId}", HttpMethod.DELETE, request, Void.class, 102, 102);
        int afterCount = clinicalExaminationDao.getAll().size();
        ClinicalExamination afterExamination = clinicalExaminationDao.getByKey(102L);

        Assert.assertNull(afterExamination);
        Assert.assertEquals(--beforeCount, afterCount);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // + delete clinical examination by id - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testDeleteClinicalExaminationError404examination() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/exam/{examinationId}", HttpMethod.DELETE, request, ExceptionDto.class, 33, 102);
        int afterCount = clinicalExaminationDao.getAll().size();
        ClinicalExamination afterExamination = clinicalExaminationDao.getByKey(101L);

        Assert.assertNotNull(afterExamination);
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // delete clinical examination by id - error 404 clinical examination not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testDeleteClinicalExaminationError404pet() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/exam/{examinationId}", HttpMethod.DELETE, request, ExceptionDto.class, 102, 33);
        int afterCount = clinicalExaminationDao.getAll().size();

        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("clinical examination not found"));
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // - delete clinical examination by id - error clinical examination not assigned to pet
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testDeleteClinicalExaminationError400refPetExamination() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/exam/{examinationId}", HttpMethod.DELETE, request, ExceptionDto.class, 101, 102);
        int afterCount = clinicalExaminationDao.getAll().size();
        ClinicalExamination afterExamination = clinicalExaminationDao.getByKey(102L);

        Assert.assertNotNull(afterExamination);
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("clinical examination not assigned to this pet"));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // - delete clinical examination by id - error pet not yours
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testDeleteClinicalExaminationError400refClientPet() {
        int beforeCount = clinicalExaminationDao.getAll().size();
        HttpEntity<Void> request = new HttpEntity<>(HEADERS);
        ResponseEntity<ExceptionDto> response = testRestTemplate
                .exchange(URI + "/{petId}/exam/{examinationId}", HttpMethod.DELETE, request, ExceptionDto.class, 100, 100);
        int afterCount = clinicalExaminationDao.getAll().size();
        ClinicalExamination afterExamination = clinicalExaminationDao.getByKey(100L);

        Assert.assertNotNull(afterExamination);
        Assert.assertEquals(beforeCount, afterCount);
        Assert.assertEquals(response.getBody(), new ExceptionDto("pet not yours"));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
