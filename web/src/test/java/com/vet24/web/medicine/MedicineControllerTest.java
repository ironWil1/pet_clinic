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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(value = "manager@gmail.com")
public class MedicineControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    private MedicineDaoImpl medicineDao;

    private final String URI = "http://localhost:8090/api/manager/medicine";

    MedicineDto medicineDtoNew;
    MedicineDto medicineDto1;
    MedicineDto medicineDto3;

    @Before
    public void createNewMedicineAndMedicineDto() {
        this.medicineDtoNew = new MedicineDto(4L, "tetetete", "etetete", "ttrtrt", "ttrrtr");
        this.medicineDto1 = new MedicineDto(100L, "tetetete", "etetete", "ttrtrt", "ttrrtr");
        this.medicineDto3 = new MedicineDto(102L, "tetetete", "etetete", "ttrtrt", "ttrrtr");
    }

    // +mock, get medicine by id
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/medicine.yml"})
    public void shouldBeGetMedicineById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{id}", 100))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // +mock, add medicine
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/medicine.yml"})
    public void shouldBeAddMedicine() throws Exception {
        int beforeCount = medicineDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "")
                .content(objectMapper.valueToTree(medicineDtoNew).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
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
                .content(objectMapper.valueToTree(medicineDto3).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
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
                .file(mockMultipartFile).header("Content-Type", "multipart/form-data"))
                .andExpect(status().isOk());
        AssertionsForClassTypes.assertThat(beforeCount).isEqualTo(medicineDao.getAll().size());
    }

    // +mock, delete medicine by id
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/medicine.yml"})
    public void shouldBeDeleteMedicine() throws Exception {
        int beforeCount = medicineDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 100)
                .content(objectMapper.valueToTree(medicineDto3).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        AssertionsForClassTypes.assertThat(--beforeCount).isEqualTo(medicineDao.getAll().size());
    }
}
