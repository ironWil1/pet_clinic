package com.vet24.web.controllers.found;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.pet.PetFoundDto;
import com.vet24.models.pet.PetContact;
import com.vet24.service.pet.PetContactService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@Slf4j
public class PetFoundControllerTest extends ControllerAbstractIntegrationTest {

    private final String URL = "/api/petFound";
    private String token;

    @Autowired
    private PetContactService petContactService;

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("client1@email.com","client");
    }

    // get save data found pet and create with send owner message about pet - success
    @Test
    @DataSet(cleanBefore = true,
            value = {"/datasets/pet-found.yml",
                    "/datasets/pet-contact.yml",
                    "/datasets/user-entities.yml",
                    "/datasets/pet-entities.yml"})
    public void testSaveDataFoundPetAndSendOwnerPetMessage() throws Exception {
        PetContact petContact = petContactService.getByKey(104L);
        String petCode = petContact.getPetCode();

        PetFoundDto petFoundDto = new PetFoundDto("1.2345678", "2.3456789", "Some text");
        String bodyUpdate = objectMapper.valueToTree(petFoundDto).toString();
        Mockito.doNothing()
                .when(mailService)
                .sendGeolocationPetFoundMessage(any(PetContact.class), anyString(), anyString());
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .header("Authorization", "Bearer " + token)
                        .content(bodyUpdate).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("petCode", petCode))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    // get PetContact by petCode is not found - error 404
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/pet-found.yml", "/datasets/pet-contact.yml", "/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void testSaveDataFoundPetAndSendOwnerPetMessageError404Pet() throws Exception {
        String petCode = "CD0964F7A769B65E2BA57822840B0E53";

        PetFoundDto petFoundDto = new PetFoundDto("1.2345678", "2.3456789", "Some text");
        String bodyUpdate = objectMapper.valueToTree(petFoundDto).toString();
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .header("Authorization", "Bearer " + token)
                        .content(bodyUpdate).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("petCode", petCode))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
        Mockito.verify(mailService, times(0))
                .sendGeolocationPetFoundMessage(any(PetContact.class), anyString(), anyString());
    }
}
