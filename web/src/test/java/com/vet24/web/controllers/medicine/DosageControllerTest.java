package com.vet24.web.controllers.medicine;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.medicine.DosageRequestDto;
import com.vet24.models.enums.DosageType;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DosageControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/manager/medicine";
    private String token;

    DosageRequestDto dosageRequestDto;
    DosageRequestDto dosageRequestDtoExists;

    @Before
    public void createNewDosageAndDosageDto() {
        this.dosageRequestDto = new DosageRequestDto(111, DosageType.DROPS);
        this.dosageRequestDtoExists = new DosageRequestDto(102, DosageType.PILLS );

    }

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("manager1@email.com","manager");
    }

    private long getCountDosages(Long medicineId) {
        return entityManager.createQuery("SELECT COUNT(d) FROM Dosage d WHERE d.medicine.id = :medicineId", Long.class).setParameter("medicineId",medicineId).getSingleResult();
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/medicineController/user-entities.yml",
            "/datasets/controllers/medicineController/medicine.yml",
            "/datasets/controllers/medicineController/dosage.yml"})
    public void getAllDosagesSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{medicineId}/dosage", 101L)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/medicineController/user-entities.yml",
            "/datasets/controllers/medicineController/medicine.yml",
            "/datasets/controllers/medicineController/dosage.yml"})
    public void createDosageSuccess() throws Exception {
        long beforeCount = getCountDosages(100L);
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/{medicineId}/dose", 100L)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(dosageRequestDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        assertThat(++beforeCount).isEqualTo(getCountDosages(100L));
    }

    //400 - Данная Дозировка уже существует
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/medicineController/user-entities.yml",
            "/datasets/controllers/medicineController/medicine.yml",
            "/datasets/controllers/medicineController/dosage.yml"})
    public void createExistDosage() throws Exception {
        long beforeCount = getCountDosages(101L);
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/{medicineId}/dose", 101L)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(dosageRequestDtoExists).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(getCountDosages(101L));
    }


    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/medicineController/user-entities.yml",
            "/datasets/controllers/medicineController/medicine.yml",
            "/datasets/controllers/medicineController/dosage.yml"})
    public void deleteDosageSuccess() throws Exception {
        long beforeCount = getCountDosages(101L);
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{medicineId}/dosage/{dosageId}", 101L, 102L)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(--beforeCount).isEqualTo(getCountDosages(101L));
    }

    //404 - Данная Дозировка не найдена
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/medicineController/user-entities.yml",
            "/datasets/controllers/medicineController/medicine.yml",
            "/datasets/controllers/medicineController/dosage.yml"})
    public void deleteNotFoundDosage() throws Exception {
        long beforeCount = getCountDosages(101L);
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{medicineId}/dosage/{dosageId}", 100L, 102L)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(getCountDosages(101L));
    }

}
