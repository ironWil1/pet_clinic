package com.vet24.web.medicine;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.medicine.MedicineDaoImpl;
import com.vet24.models.dto.medicine.MedicineDto;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MedicineControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    private MedicineDaoImpl medicineDao;

    private final String URI = "/api/manager/medicine";
    private String token;

    MedicineDto medicineDtoNew;
    MedicineDto medicineDto1;
    MedicineDto medicineDto3;

    @Before
    public void createNewMedicineAndMedicineDto() {
        this.medicineDtoNew = new MedicineDto(4L, "tetetete", "etetete", "ttrtrt", "ttrrtr");
        this.medicineDto1 = new MedicineDto(100L, "tetetete", "etetete", "ttrtrt", "ttrrtr");
        this.medicineDto3 = new MedicineDto(102L, "tetetete", "etetete", "ttrtrt", "ttrrtr");
    }

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("manager1@email.com","manager");
    }

    // +mock, get medicine by id
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/medicine.yml"})
    public void shouldBeGetMedicineById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{id}", 100)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // +mock, add medicine
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/medicine.yml"})
    public void shouldBeAddMedicine() throws Exception {
        int beforeCount = medicineDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(medicineDtoNew).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(medicineDtoNew)));
        AssertionsForClassTypes.assertThat(++beforeCount).isEqualTo(medicineDao.getAll().size());
    }

    // +mock, put medicine by id
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/medicine.yml"})
    public void shouldBeUpdateMedicineById() throws Exception {
        int beforeCount = medicineDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 101)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(medicineDto3).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(medicineDto3)));
        AssertionsForClassTypes.assertThat(beforeCount).isEqualTo(medicineDao.getAll().size());
    }

    // +mock, upload icon for medicine by id
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/medicine.yml"})
    public void shouldBeUpdateMedicineIcon() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("test.png");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",
                classPathResource.getFilename(), null, classPathResource.getInputStream());
        int beforeCount = medicineDao.getAll().size();
        mockMvc.perform(multipart(URI + "/{id}/set-pic", 100)
                        .file(mockMultipartFile).header("Content-Type", "multipart/form-data")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        AssertionsForClassTypes.assertThat(beforeCount).isEqualTo(medicineDao.getAll().size());
    }

    // +mock, delete medicine by id
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/medicine.yml"})
    public void shouldBeDeleteMedicine() throws Exception {
        int beforeCount = medicineDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(medicineDto3).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        AssertionsForClassTypes.assertThat(--beforeCount).isEqualTo(medicineDao.getAll().size());
    }
}
