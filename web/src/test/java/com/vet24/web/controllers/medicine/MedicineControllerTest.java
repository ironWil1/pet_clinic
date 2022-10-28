package com.vet24.web.controllers.medicine;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.medicine.MedicineRequestDto;
import com.vet24.models.medicine.Medicine;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MedicineControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/manager/medicine";
    private String token;
    private int initialCount = 3;

    MedicineRequestDto medicineRequestDto;
    MedicineRequestDto medicineRequestEmptyFirstField;
    MedicineRequestDto medicineRequestEmptySecondField;
    MedicineRequestDto medicineRequestEmptyThirdField;
    MedicineRequestDto medicineRequestEmptyFourthField;
    MedicineRequestDto medicineRequestDto1;

    @Before
    public void createNewMedicineAndMedicineDto() {
        this.medicineRequestDto = new MedicineRequestDto("aaa", "aaa", "aaa", "aaa");
        this.medicineRequestDto1 = new MedicineRequestDto("ccc", "ccc", "ccc", "ccc");
        this.medicineRequestEmptyFirstField = new MedicineRequestDto(null, "bbb", "bbb", "bbb");
        this.medicineRequestEmptySecondField = new MedicineRequestDto("bbb", null, "bbb", "bbb");
        this.medicineRequestEmptyThirdField = new MedicineRequestDto("bbb", "bbb", null, "bbb");
        this.medicineRequestEmptyFourthField = new MedicineRequestDto("bbb", "bbb", "bbb", null);
    }

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("manager1@email.com","manager");
    }

    private long getCount() {
        return entityManager.createQuery("SELECT COUNT(m) FROM Medicine m", Long.class).getSingleResult();
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/medicineController/user-entities.yml",
            "/datasets/controllers/medicineController/medicine.yml"})
    public void getMedicineByIdSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{id}", 100L)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.description").value("test_description1"))
                .andExpect(jsonPath("$.iconUrl").value("test.png"))
                .andExpect(jsonPath("$.manufactureName").value("test_manufacture_name1"))
                .andExpect(jsonPath("$.name").value("name1"))
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.creationDateTime").value("2022-02-11T00:00:00"))
                .andExpect(jsonPath("$.lastUpdateDateTime").value("2022-03-19T00:00:00"));
    }

    // Препарата с таким ID не существует для GET запроса одного препарата
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/medicineController/user-entities.yml",
            "/datasets/controllers/medicineController/medicine.yml"})
    public void getNonExistingMedicine() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{id}", 1000L)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/medicineController/user-entities.yml",
            "/datasets/controllers/medicineController/medicine.yml"})
    public void deleteMedicineSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 100L)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(--initialCount).isEqualTo(getCount());
    }

    // Препарата с таким ID не существует для DELETE запроса
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/medicineController/user-entities.yml",
            "/datasets/controllers/medicineController/medicine.yml"})
    public void deleteNonExistingMedicine() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 1000L)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/medicineController/user-entities.yml",
            "/datasets/controllers/medicineController/medicine.yml"})
    public void updateMedicineSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 100L)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(medicineRequestDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Medicine medicine = entityManager.find(Medicine.class, 100L);
        assertThat(medicine.getManufactureName()).isEqualTo("aaa");
        assertThat(medicine.getName()).isEqualTo("aaa");
        assertThat(medicine.getDescription()).isEqualTo("aaa");
        assertThat(medicine.getIconUrl()).isEqualTo("aaa");
        assertThat(medicine.getId()).isEqualTo(100);
        assertThat(medicine.getCreationDateTime()).isEqualTo("2022-02-11T00:00:00");
        assertThat(medicine.getLastUpdateDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).isEqualTo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        assertThat(medicine.getLastUpdateAuthor().getProfile().getLastName()).isEqualTo("Manager");
        assertThat(medicine.getLastUpdateAuthor().getProfile().getFirstName()).isEqualTo("Manager");
        assertThat(medicine.getLastUpdateAuthor().getEmail()).isEqualTo("manager1@email.com");
        assertThat(medicine.getLastUpdateAuthor().getId()).isEqualTo(101);
    }

    // Препарата с таким ID не существует для PUT запроса
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/medicineController/user-entities.yml",
            "/datasets/controllers/medicineController/medicine.yml"})
    public void updateNonExistingMedicine() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 1000L)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(medicineRequestDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

// Были заполнены не все поля при изменении существующего Препарата (1 поле)
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/medicineController/user-entities.yml",
            "/datasets/controllers/medicineController/medicine.yml"})
    public void updateMedicineEmptyFirstField() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 100L)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(medicineRequestEmptyFirstField).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // Были заполнены не все поля при изменении существующего Препарата (2 поле)
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/medicineController/user-entities.yml",
            "/datasets/controllers/medicineController/medicine.yml"})
    public void updateMedicineEmptySecondField() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 100L)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(medicineRequestEmptySecondField).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // Были заполнены не все поля при изменении существующего Препарата (3 поле)
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/medicineController/user-entities.yml",
            "/datasets/controllers/medicineController/medicine.yml"})
    public void updateMedicineEmptyThirdField() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 100L)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(medicineRequestEmptyThirdField).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // Были заполнены не все поля при изменении существующего Препарата (4 поле)
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/medicineController/user-entities.yml",
            "/datasets/controllers/medicineController/medicine.yml"})
    public void updateMedicineEmptyFourthField() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 100L)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(medicineRequestEmptyFourthField).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/medicineController/user-entities.yml",
            "/datasets/controllers/medicineController/medicine.yml"})
    public void createMedicineSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(medicineRequestDto1).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        assertThat(++initialCount).isEqualTo(getCount());
    }

    // Были заполнены не все поля при создании нового Препарата (1 поле)
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/medicineController/user-entities.yml",
            "/datasets/controllers/medicineController/medicine.yml"})
    public void createMedicineEmptyFirstField() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(medicineRequestEmptyFirstField).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // Были заполнены не все поля при создании нового Препарата (2 поле)
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/medicineController/user-entities.yml",
            "/datasets/controllers/medicineController/medicine.yml"})
    public void createMedicineEmptySecondField() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(medicineRequestEmptySecondField).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // Были заполнены не все поля при создании нового Препарата (3 поле)
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/medicineController/user-entities.yml",
            "/datasets/controllers/medicineController/medicine.yml"})
    public void createMedicineEmptyThirdField() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(medicineRequestEmptyThirdField).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // Были заполнены не все поля при создании нового Препарата (4 поле)
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/medicineController/user-entities.yml",
            "/datasets/controllers/medicineController/medicine.yml"})
    public void createMedicineEmptyFourthField() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(medicineRequestEmptyFourthField).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/medicineController/user-entities.yml",
            "/datasets/controllers/medicineController/medicine.yml"})
    public void searchSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                        .param("manufactureName","")
                        .param("name","name1")
                        .param("searchText","")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
