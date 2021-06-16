package com.vet24.web.pet.clinicalexamination;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.vet24.dao.pet.PetDao;
import com.vet24.dao.pet.clinicalexamination.ClinicalExaminationDao;
import com.vet24.dao.user.DoctorDao;
import com.vet24.models.dto.pet.clinicalexamination.ClinicalExaminationDto;
import com.vet24.models.dto.pet.reproduction.ReproductionDto;
import com.vet24.models.mappers.pet.clinicalexamination.ClinicalExaminationMapper;
import com.vet24.models.pet.clinicalexamination.ClinicalExamination;
import com.vet24.service.pet.clinicalexamination.ClinicalExaminationService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import com.vet24.web.controllers.pet.clinicalexamination.ClinicalExaminationController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

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
    // client 3 --> pet 2 --> reproduction 2 (to check pet-reproduction link)
    //        `---> pet 3 --> reproduction 3 (to get & update & delete)
    //                  `---> reproduction 4 (to create)

    @Before
    public void createNewReproductionAndReproductionDto() {
        this.clinicalExaminationDtoNew = new ClinicalExaminationDto(4L, 10, true, "textNew");
        this.clinicalExaminationDto1 = new ClinicalExaminationDto(100L, 20, true, "text1");
        this.clinicalExaminationDto3 = new ClinicalExaminationDto(102L, 40, true, "text3");
    }

    // get clinical examination by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/clinical-examination.yml"})
    public void testGetClinicalExaminationSuccess() {
        ClinicalExaminationDto dtoFromDao = clinicalExaminationMapper.clinicalExaminationToClinicalExaminationDto(clinicalExaminationDao.getByKey(102L));
        ResponseEntity<ClinicalExaminationDto> response = testRestTemplate
                .getForEntity(URI + "/{petId}/exam/{examinationId}", ClinicalExaminationDto.class, 102, 102);

        Assert.assertEquals(dtoFromDao, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
