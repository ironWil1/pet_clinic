package com.vet24.web.controllers.pet.appearance;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class AppearanceControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/appearance";
    private String token;

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("admin1@email.com", "admin");
    }

    // ответ на запрос с ошибкой - нужный цвет
    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/appearanceController/user-entities.yml",
            "datasets/controllers/appearanceController/pet_color.yml"})
    public void findColorIfRequestIsInError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/color")
                        .param("text", "greyyy")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0]", Is.is("grey")));
    }

    // ответ на запрос с случайным набором символов - пустой список
    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/appearanceController/user-entities.yml",
            "datasets/controllers/appearanceController/pet_color.yml"})
    public void getListEmptyColorIfRequestIsInvalid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/color")
                        .param("text", "grerwerygdfg")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Matchers.empty()));

    }

    //ответ на запрос без параметров - список всех расцветок
    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/appearanceController/user-entities.yml",
            "datasets/controllers/appearanceController/pet_color.yml"})
    public void getListColorsIfRequestParamsIsNull() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/color")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.contains("black", "grey")));
    }

    //оответ на запрос с пустым параметром - список всех расцветок
    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/appearanceController/user-entities.yml",
            "datasets/controllers/appearanceController/pet_color.yml"})
    public void getListColorsIfRequestParamsIsEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/color")
                        .param("text", "")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.contains("black", "grey")));
    }

    //ответ на запрос с ошибкой - нужная порода
    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/appearanceController/user-entities.yml",
            "datasets/controllers/appearanceController/pet_breed.yml"})
    public void getBreedIfRequestIsInError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/breed")
                        .param("petType", "dog")
                        .param("text", "бигльбв")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0]", Is.is("бигль")));
    }

    //ответ на запрос без параметров - список всех пород
    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/appearanceController/user-entities.yml",
            "datasets/controllers/appearanceController/pet_breed.yml"})
    public void getListBreedsIfRequestParamsIsNull() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/breed")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.contains("бигль", "мопс", "сфинкс")));
    }

    //ответ на запрос с пустыми параметрами - список всех пород
    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/appearanceController/user-entities.yml",
            "datasets/controllers/appearanceController/pet_breed.yml"})
    public void getListBreedsIfRequestParamsIsEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/breed")
                        .param("petType", "")
                        .param("text", "")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.contains("бигль", "мопс", "сфинкс")));
    }

    // ответ на запрос с параметром типа животного - список всех пород этого типа
    @Test
    @DataSet(cleanBefore = true, value = {
            "datasets/controllers/appearanceController/user-entities.yml",
            "datasets/controllers/appearanceController/pet_breed.yml"})
    public void getListBreedsByPetType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/breed")
                        .param("petType", "dog")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.contains("бигль", "мопс")));
    }
}
