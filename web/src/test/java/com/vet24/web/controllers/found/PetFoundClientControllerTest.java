package com.vet24.web.controllers.found;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.web.ControllerAbstractIntegrationTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class PetFoundClientControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/client/petFound";
    private String token;

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("client1@email.com", "client");
    }

    //История находок питомца успешно получена - 200 SUCCESS
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/petFoundClientController/user-entities.yml",
            "/datasets/controllers/petFoundClientController/pet-entities.yml",
            "/datasets/controllers/petFoundClientController/pet-found.yml"})
    public void testGetHistoryPetByIdSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                                .param("petId", "101")
                                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].latitude").value("1.2345678"))
                .andExpect(jsonPath("$[0].longitude").value("2.3456789"))
                .andExpect(jsonPath("$[0].message").value("Some text"))
                .andExpect(jsonPath("$[0].foundDate").value("2022-11-27T20:09:00.712"));
    }

    //История находок питомца не найдена - 400 ERROR "pet doesn't belong to the client"
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/petFoundClientController/user-entities.yml",
            "/datasets/controllers/petFoundClientController/pet-entities.yml",
            "/datasets/controllers/petFoundClientController/pet-found.yml"})
    public void testGetHistoryPetByIdBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                                .param("petId", "102")
                                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //Указан неверный id питомца - 404 ERROR "pet not found"
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/petFoundClientController/user-entities.yml",
            "/datasets/controllers/petFoundClientController/pet-entities.yml",
            "/datasets/controllers/petFoundClientController/pet-found.yml"})
    public void testGetHistoryPetByIdNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                                .param("petId", "100")
                                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}