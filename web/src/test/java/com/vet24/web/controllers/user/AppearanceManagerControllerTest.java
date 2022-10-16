package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AppearanceManagerControllerTest extends ControllerAbstractIntegrationTest {


    private final String URI = "/api/manager/appearance";

    private String token;

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("manager1@email.com", "manager");
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/user-entities.yml",
            "datasets/pet-breed.yml"})
    public void getBreedTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/breed")
                        .param("petType", "DOG")
                        .param("breed", "cHaoTIc")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0]", Is.is("chaotic")));
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/user-entities.yml",
            "datasets/pet-breed.yml"})
    public void postBreedBadRequestPetType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/breed")
                        .param("petType", "")
                        .param("breed","chaotic")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/user-entities.yml",
            "datasets/pet-breed.yml"})
    public void deleteBreedBadRequestPetType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/breed")
                        .param("petType", "")
                        .param("breed","chaotic")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

//    @Test
//    @DataSet(cleanBefore = true, value = {
//            "datasets/user-entities.yml",
//            "datasets/pet-breed.yml"})
//    public void postBreedTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/breed")
//                        .param("petType", "FISH")
//                        .param("breed", "Goldfish")
//                        .header("Authorization", "Bearer " + token))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//        assertThat(breedService.getBreed("FISH", "Goldfish")).isEqualTo("Goldfish");
//    }

//    @Test
//    @DataSet(cleanBefore = true, value = {
//            "datasets/user-entities.yml",
//            "datasets/pet-breed.yml"})
//    public void deleteBreedTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/breed")
//                        .param("petType", "DOG")
//                        .param("breed", "cHaoTIc")
//                        .header("Authorization", "Bearer " + token))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//        assertThat(breedService.getBreed("DOG", "chaotic")).isEqualTo("");
//    }


}
