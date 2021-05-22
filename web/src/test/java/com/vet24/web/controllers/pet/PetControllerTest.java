package com.vet24.web.controllers.pet;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.vet24.dao.pet.PetDao;
import com.vet24.models.dto.pet.AbstractNewPetDto;
import com.vet24.models.dto.pet.DogDto;
import com.vet24.models.dto.pet.PetDto;
import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetSize;
import com.vet24.models.enums.PetType;
import com.vet24.models.pet.Pet;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DBRider
public class PetControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    private PetController controller;

    @Autowired
    private PetDao petDao;

    private final String URI = "http://localhost:8090/api/client/pet";

    private AbstractNewPetDto abstractNewPetDto;

    @Before
    public void createNewClientAndDog() {
        this.abstractNewPetDto = new DogDto("name", PetType.DOG, LocalDate.now(), Gender.MALE, "breed",
                "color", PetSize.MEDIUM, 9.3, "description", "test.png", 0);
    }

    @Test
    public void doesPetControllerExist() {
        assertThat(controller).isNotNull();
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void persistPetSuccess() {
        int sizeBefore = petDao.getAll().size();
        HttpEntity<AbstractNewPetDto> request = new HttpEntity<>(abstractNewPetDto, new HttpHeaders());
        ResponseEntity<AbstractNewPetDto> response = testRestTemplate
                .postForEntity(URI + "/add", request, AbstractNewPetDto.class);
        int sizeAfter = petDao.getAll().size();

        Assert.assertEquals(++sizeBefore, sizeAfter);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void deletePetSuccess() {
        int sizeBefore = petDao.getAll().size();
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<Void> response = testRestTemplate
                .exchange(URI + "/{petId}", HttpMethod.DELETE, new HttpEntity<>(headers), Void.class, 102);
        int sizeAfter = petDao.getAll().size();
        Pet pet = petDao.getByKey(102L);

        Assert.assertNull(pet);
        Assert.assertEquals(--sizeBefore, sizeAfter);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void deletePetOfAnotherOwnerBadRequest() {
        int sizeBefore = petDao.getAll().size();
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<Void> response = testRestTemplate
                .exchange(URI + "/{petId}", HttpMethod.DELETE, new HttpEntity<>(headers), Void.class, 100);
        int sizeAfter = petDao.getAll().size();
        Pet pet = petDao.getByKey(100L);

        Assert.assertNotNull(pet);
        Assert.assertEquals(sizeBefore, sizeAfter);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void deletePetThatDoesNotExistNotFound() {
        Pet petBefore = petDao.getByKey(69000L);
        int sizeBefore = petDao.getAll().size();
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<Void> response = testRestTemplate
                .exchange(URI + "/{petId}", HttpMethod.DELETE, new HttpEntity<>(headers), Void.class, 69000);
        int sizeAfter = petDao.getAll().size();

        Assert.assertNull(petBefore);
        Assert.assertEquals(sizeBefore, sizeAfter);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void updatePetSuccess() {
        Pet petBefore = petDao.getByKey(102L);
        int sizeBefore = petDao.getAll().size();
        HttpEntity<AbstractNewPetDto> request = new HttpEntity<>(abstractNewPetDto, new HttpHeaders());
        ResponseEntity<PetDto> response = testRestTemplate
                .exchange(URI + "/{petId}", HttpMethod.PUT, request, PetDto.class, 102);
        Pet petAfter = petDao.getByKey(102L);
        int sizeAfter = petDao.getAll().size();

        Assert.assertEquals(sizeBefore, sizeAfter);
        Assert.assertNotEquals(petBefore, petAfter);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void updatePetOfAnotherOwnerBadRequest() {
        Pet petBefore = petDao.getByKey(100L);
        int sizeBefore = petDao.getAll().size();
        HttpEntity<AbstractNewPetDto> request = new HttpEntity<>(abstractNewPetDto, new HttpHeaders());
        ResponseEntity<PetDto> response = testRestTemplate
                .exchange(URI + "/{petId}", HttpMethod.PUT, request, PetDto.class, 100);
        Pet petAfter = petDao.getByKey(100L);
        int sizeAfter = petDao.getAll().size();

        Assert.assertEquals(sizeBefore, sizeAfter);
        Assert.assertEquals(petBefore.getId(), petAfter.getId());
        Assert.assertEquals(petBefore.getPetType(), petAfter.getPetType());
        Assert.assertEquals(petBefore.getName(), petAfter.getName());
        Assert.assertEquals(petBefore.getBirthDay(), petAfter.getBirthDay());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void updatePetThatDoesNotExistNotFound() {
        Pet petBefore = petDao.getByKey(69000L);
        int sizeBefore = petDao.getAll().size();
        HttpEntity<AbstractNewPetDto> request = new HttpEntity<>(abstractNewPetDto, new HttpHeaders());
        ResponseEntity<PetDto> response = testRestTemplate
                .exchange(URI + "/{petId}", HttpMethod.PUT, request, PetDto.class, 69000);
        int sizeAfter = petDao.getAll().size();

        Assert.assertNull(petBefore);
        Assert.assertEquals(sizeBefore, sizeAfter);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void getPetAvatarSuccess() {
        persistPetAvatarSuccess();
        ResponseEntity<byte[]> response = testRestTemplate
                .getForEntity(URI + "/{petId}/avatar", byte[].class, 102);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void getPetAvatarButPetDoesNotExistNotFound() {
        ResponseEntity<byte[]> response = testRestTemplate
                .getForEntity(URI + "/{petId}/avatar", byte[].class, 69000);

        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void persistPetAvatarSuccess() {
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("file", new ClassPathResource("test.png"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);
        ResponseEntity<String> response = testRestTemplate
                .exchange(URI + "/{petId}/avatar", HttpMethod.POST, entity, String.class, 102);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void persistPetAvatarOfAnotherOwnerBadRequest() {
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("file", new ClassPathResource("test.png"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);
        ResponseEntity<String> response = testRestTemplate
                .exchange(URI + "/{petId}/avatar", HttpMethod.POST, entity, String.class, 100);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void persistPetAvatarButPetDoesNotExistNotFound() {
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("file", new ClassPathResource("test.png"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);
        ResponseEntity<String> response = testRestTemplate
                .exchange(URI + "/{petId}/avatar", HttpMethod.POST, entity, String.class, 69000);

        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}