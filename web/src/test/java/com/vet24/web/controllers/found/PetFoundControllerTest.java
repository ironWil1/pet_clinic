package com.vet24.web.controllers.found;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.vet24.models.dto.pet.PetFoundDto;
import com.vet24.models.pet.PetContact;
import com.vet24.service.pet.PetContactService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@DBRider
@WithUserDetails(value = "client1@email.com")
public class PetFoundControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "http://localhost:8090/api/petFound";

    @Autowired
    PetContactService petContactService;

    // get save data found pet and create with send owner message about pet - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/pet-found.yml", "/datasets/pet-contact.yml", "/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void testSaveDataFoundPetAndSendOwnerPetMessage() {
        PetContact petContact = petContactService.getByKey(104L);
        PetFoundDto petFoundDto = new PetFoundDto("1.2345678", "2.3456789", "Some text");
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(URI)
                .queryParam("petCode", petContact.getPetCode())
                .build().encode();
        ResponseEntity<PetFoundDto> response = testRestTemplate.postForEntity(uriComponents.toUri(), petFoundDto, PetFoundDto.class);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    // get PetContact by petCode is not found - error 404
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/pet-found.yml", "/datasets/pet-contact.yml", "/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void testSaveDataFoundPetAndSendOwnerPetMessageError404Pet() {
        PetFoundDto petFoundDto = new PetFoundDto("1.2345678", "2.3456789", "Some text");
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(URI)
                .queryParam("petCode", "CD0964F7A769B65E2BA57822840B0E53")
                .build().encode();
        ResponseEntity<PetFoundDto> response = testRestTemplate.postForEntity(uriComponents.toUri(), petFoundDto, PetFoundDto.class);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
