package com.vet24.web.medicine;

import com.vet24.models.dto.medicine.MedicineDto;
import com.vet24.models.mappers.medicine.MedicineMapper;
import com.vet24.models.medicine.Medicine;
import com.vet24.web.ControllerAbstractIntegrationTest;
import com.vet24.web.controllers.medicine.MedicineController;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

public class MedicineControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    MedicineMapper medicineMapper;

    @Autowired
    MedicineController medicineController;

    final String URI = "http://localhost:8090/api/manager/medicine";

    @Test
    public void getMedicineController() throws Exception {
        assertThat(medicineController).isNotNull();
    }

    @Test
    public void testGetMedicineSuccess() throws Exception {
        ResponseEntity<MedicineDto> response = testRestTemplate
                .getForEntity(URI + "/{id}", MedicineDto.class, 1);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testPutMedicineSuccess() throws Exception {
        MedicineDto medicineDto = new MedicineDto();
        medicineDto.setName("jm");
        medicineDto.setDescription("java");
        medicineDto.setManufactureName("mentor");
        medicineDto.setId(1L);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MedicineDto> entity = new HttpEntity<>(medicineDto, headers);
        ResponseEntity<MedicineDto> response =  testRestTemplate
                .exchange(URI + "/{id}", HttpMethod.PUT, entity, MedicineDto.class, 1);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteMedicineSuccess() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response =  testRestTemplate
                .exchange(URI + "/{id}", HttpMethod.DELETE, entity, Void.class, 1);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddMedicineSuccess() throws URISyntaxException
    {
        Medicine medicine = new Medicine("daulet", "jm", "dsad", "test");
        MedicineDto medicineDto =  medicineMapper.medicineToMedicineDto(medicine);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MedicineDto> request = new HttpEntity<>(medicineDto, headers);
        ResponseEntity<MedicineDto> response = testRestTemplate
                .postForEntity(URI, request, MedicineDto.class);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
