package com.vet24.web.medicine;

import com.vet24.models.dto.medicine.MedicineDto;
import com.vet24.models.mappers.medicine.MedicineMapper;
import com.vet24.models.medicine.Medicine;
import com.vet24.web.ControllerAbstractIntegrationTest;
import com.vet24.web.controllers.medicine.MedicineController;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MedicineControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    MedicineMapper medicineMapper;

    @Autowired
    MedicineController medicineController;

    final String URI = "http://localhost:8090/api/manager/medicine";

    //test controller exist
    @Test
    public void getAMedicineController() throws Exception {
        assertThat(medicineController).isNotNull();
    }

    //get medicine by id
    @Test
    public void testBGetMedicineSuccess() throws Exception {
        ResponseEntity<MedicineDto> response = testRestTemplate
                .getForEntity(URI + "/{id}", MedicineDto.class, 1);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //add medicine
    @Test
    public void testCAddMedicineSuccess() throws URISyntaxException
    {
        Medicine medicine = new Medicine("daulet", "jm", "dsad", "test");
        MedicineDto medicineDto =  medicineMapper.medicineToMedicineDto(medicine);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MedicineDto> request = new HttpEntity<>(medicineDto, headers);
        ResponseEntity<MedicineDto> response = testRestTemplate
                .postForEntity(URI, request, MedicineDto.class);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    //put medicine by id
    @Test
    public void testDPutMedicineSuccess() throws Exception {
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

    //upload icon for medicine by id
    @Test
    public void testESetMedicineIconSuccess() throws Exception {
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("file", new org.springframework.core.io.ClassPathResource("test.png"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);
        ResponseEntity<String> response = testRestTemplate
                .exchange(URI + "/{id}/set-pic", HttpMethod.POST, entity, String.class, 1);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //get icon for medicine by id
    @Test
    public void testFGetMedicineIconSuccess() throws Exception {
        ResponseEntity<byte[]> response = testRestTemplate
                .getForEntity(URI + "/{id}/set-pic", byte[].class, 1);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //delete medicine by id
    @Test
    public void testGDeleteMedicineSuccess() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response =  testRestTemplate
                .exchange(URI + "/{id}", HttpMethod.DELETE, entity, Void.class, 2);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //test search medicine
    @Test
    public void testHSearch() throws Exception {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URI + "/search")
                .queryParam("manufactureName")
                .queryParam("name")
                .queryParam("searchText", "java");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                entity,
                String.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
