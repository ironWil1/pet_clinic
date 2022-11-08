package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.service.pet.appearance.BreedService;
import com.vet24.service.pet.appearance.ColorService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class AppearanceManagerControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/manager/appearance";

    @Autowired
    private BreedService breedService;

    @Autowired
    private ColorService colorService;

    private String token;

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("manager1@email.com", "manager");
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/user/appearanceManagerController/user_entities.yml",
            "datasets/controllers/user/appearanceManagerController/pet_breed.yml"})
    public void getBreedSuccessTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/breed")
                        .param("petType", "DOG")
                        .param("breed", "chaotic")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0]", Is.is("chaotic")));
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/user/appearanceManagerController/user_entities.yml",
            "datasets/controllers/user/appearanceManagerController/pet_breed.yml"})
    public void getBreedEmptyParamTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/breed")
                        .param("petType", "")
                        .param("breed", "")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$",
                        Is.is(entityManager.
                                createNativeQuery("select breed from pet_breed").getResultList())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/user/appearanceManagerController/user_entities.yml",
            "datasets/controllers/user/appearanceManagerController/pet_breed.yml"})
    public void getBreedEmptyPetTypeTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/breed")
                        .param("petType", "")
                        .param("breed", "chaotic")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Is.is(List.of("chaotic","chaotic"))));
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/user/appearanceManagerController/user_entities.yml",
            "datasets/controllers/user/appearanceManagerController/pet_breed.yml"})
    public void getBreedEmptyBreedTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/breed")
                        .param("petType", "DOG")
                        .param("breed", "")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Is.is(breedService.getBreedsByPetType("DOG"))));
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/user/appearanceManagerController/user_entities.yml",
            "datasets/controllers/user/appearanceManagerController/pet_breed.yml"})
    public void postBreedSuccessTest() throws Exception {
        int sizeBefore = breedService.getAllBreeds().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/breed")
                        .param("petType", "DOG")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .writeValueAsString(Arrays.asList("Lawful breed", "Chaotic breed"))))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(sizeBefore+2).isEqualTo(breedService.getAllBreeds().size());
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/user/appearanceManagerController/user_entities.yml",
            "datasets/controllers/user/appearanceManagerController/pet_breed.yml"})
    public void postBreedIfBreedPresentTest() throws Exception {
        int sizeBefore = breedService.getAllBreeds().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/breed")
                        .param("petType", "DOG")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .writeValueAsString(Arrays.asList("    cHaOTiC", "EVIL      "))))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(sizeBefore).isEqualTo(breedService.getAllBreeds().size());
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/user/appearanceManagerController/user_entities.yml",
            "datasets/controllers/user/appearanceManagerController/pet_breed.yml"})
    public void postBreedEmptyPetTypeTest() throws Exception {
        int sizeBefore = breedService.getAllBreeds().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/breed")
                        .param("petType", "")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .writeValueAsString(Arrays.asList("    cHaOTiC"))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(sizeBefore).isEqualTo(breedService.getAllBreeds().size());
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/user/appearanceManagerController/user_entities.yml",
            "datasets/controllers/user/appearanceManagerController/pet_breed.yml"})
    public void deleteBreedSuccessTest() throws Exception {
        int sizeBefore = breedService.getAllBreeds().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/breed")
                        .param("petType", "DOG")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .writeValueAsString(Arrays.asList("    cHaOTiC", "evil"))))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(sizeBefore - 2).isEqualTo(breedService.getAllBreeds().size());
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/user/appearanceManagerController/user_entities.yml",
            "datasets/controllers/user/appearanceManagerController/pet_breed.yml"})
    public void deleteBreedNotPresentTest() throws Exception {
        int sizeBefore = breedService.getAllBreeds().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/breed")
                        .param("petType", "DOG")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .writeValueAsString(Arrays.asList("notExistingBreed"))))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(sizeBefore).isEqualTo(breedService.getAllBreeds().size());
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/user/appearanceManagerController/user_entities.yml",
            "datasets/controllers/user/appearanceManagerController/pet_breed.yml"})
    public void deleteBreedEmptyPetTypeTest() throws Exception {
        int sizeBefore = breedService.getAllBreeds().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/breed")
                        .param("petType", "")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .writeValueAsString(Arrays.asList("chaotic"))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(sizeBefore).isEqualTo(breedService.getAllBreeds().size());
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/user/appearanceManagerController/user_entities.yml",
            "datasets/controllers/user/appearanceManagerController/pet_color.yml"})
    public void getColorSuccessTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/color")
                        //test trim of incorrect param insert
                        .param("color", "   black")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0]", Is.is("black")));
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/user/appearanceManagerController/user_entities.yml",
            "datasets/controllers/user/appearanceManagerController/pet_color.yml"})
    public void getColorEmptyParamTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/color")
                        .param("color", "")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(colorService.getAllColors())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/user/appearanceManagerController/user_entities.yml",
            "datasets/controllers/user/appearanceManagerController/pet_color.yml"})
    public void addColorSuccessTest() throws Exception {
        int sizeBeforeTest = colorService.getAllColors().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/color")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                        .writeValueAsString(List.of("green"))))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(colorService.getAllColors().size()).isEqualTo(sizeBeforeTest + 1);
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/user/appearanceManagerController/user_entities.yml",
            "datasets/controllers/user/appearanceManagerController/pet_color.yml"})
    public void addColorIfPresentTest() throws Exception {
        int sizeBefore = colorService.getAllColors().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/color")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .writeValueAsString(List.of("black"))))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(sizeBefore).isEqualTo(colorService.getAllColors().size());
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/user/appearanceManagerController/user_entities.yml",
            "datasets/controllers/user/appearanceManagerController/pet_color.yml"})
    public void deleteColorSuccessTest() throws Exception {
        int sizeBefore = colorService.getAllColors().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/color")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .writeValueAsString(List.of("black"))))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(colorService.getAllColors().size()).isEqualTo(sizeBefore - 1);
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/user/appearanceManagerController/user_entities.yml",
            "datasets/controllers/user/appearanceManagerController/pet_color.yml"})
    public void deleteColorIfNotPresentTest() throws Exception {
        int sizeBefore = colorService.getAllColors().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/color")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .writeValueAsString(List.of("grey"))))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(sizeBefore).isEqualTo(colorService.getAllColors().size());
    }


}
