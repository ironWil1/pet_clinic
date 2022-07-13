//package com.vet24.web.pet.procedure;
//
//import com.github.database.rider.core.api.dataset.DataSet;
//import com.vet24.dao.pet.procedure.ExternalParasiteProcedureDao;
//import com.vet24.dao.pet.procedure.ProcedureDao;
//import com.vet24.models.dto.pet.procedure.AbstractNewProcedureDto;
//import com.vet24.models.dto.pet.procedure.ExternalParasiteDto;
//import com.vet24.models.dto.pet.procedure.ProcedureDto;
//import com.vet24.models.dto.pet.procedure.VaccinationDto;
//import com.vet24.models.enums.ProcedureType;
//import com.vet24.models.mappers.pet.procedure.ExternalParasiteMapper;
//import com.vet24.models.mappers.pet.procedure.ProcedureMapper;
//import com.vet24.web.ControllerAbstractIntegrationTest;
//import com.vet24.web.controllers.pet.procedure.ExternalParasiteController;
//import com.vet24.web.controllers.pet.procedure.ProcedureController;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.time.LocalDate;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//public class ProcedureControllerTest extends ControllerAbstractIntegrationTest {
//
//    @Autowired
//    ProcedureController procedureController;
//
//    @Autowired
//    ProcedureMapper procedureMapper;
//
//    @Autowired
//    ProcedureDao procedureDao;
//
//    final String URI = "/api/client/pet";
//    final HttpHeaders HEADERS = new HttpHeaders();
//    private String token;
//    AbstractNewProcedureDto newProcedureDto;
//    ProcedureDto procedureDto1;
//    ProcedureDto procedureDto3;
//
//    // client 4 --> pet 1 --> procedure 1 (to check client-pet link)
//    // client 3 --> pet 2 --> procedure 2 (to check pet-procedure link)
//    //        `---> pet 3 --> procedure 3 (to get & update & delete)
//    //                  `---> procedure 4 (to create)
//
//    @Before
//    public void createModels() {
//        this.newProcedureDto = new VaccinationDto(LocalDate.now(), 100L, "4f435", false, null);
//        this.procedureDto1 = new ProcedureDto(100L, LocalDate.now(), ProcedureType.EXTERNAL_PARASITE,
//                100L, "4f435", true, 20);
//        this.procedureDto3 = new ProcedureDto(102L, LocalDate.now(), ProcedureType.EXTERNAL_PARASITE,
//                100L, "4f435", true, 20);
//    }
//
//    @Before
//    public void setToken() throws Exception {
//        token = getAccessToken("user3@gmail.com","user3");
//    }
//
//    // +mock, GET procedure by id - 200 SUCCESS
//    @Test
//    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
//    public void testGetProcedureSuccess() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{petId}/procedure/{procedureId}", 102, 102)
//                        .header("Authorization", "Bearer " + token))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//
//    // +mock, GET procedure by id - 404 ERROR "pet not found"
//    @Test
//    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
//    public void testGetProcedureErrorPetNotFound() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{petId}/procedure/{procedureId}", 33, 102)
//                        .header("Authorization", "Bearer " + token))
//                .andExpect(MockMvcResultMatchers.status().isNotFound());
//    }
//
//    // +mock, GET procedure by id - 404 ERROR "procedure not found"
//    @Test
//    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "/datasets/reproduction.yml"})
//    public void testGetProcedureErrorProcedureNotFound() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{petId}/procedure/{procedureId}", 102, 33)
//                        .header("Authorization", "Bearer " + token))
//                .andExpect(MockMvcResultMatchers.status().isNotFound());
//    }
//
//    // +mock, GET procedure by id - 400 ERROR "pet not yours"
//    @Test
//    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
//    public void testGetProcedureErrorPetForbidden() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{petId}/procedure/{procedureId}", 100, 100)
//                        .header("Authorization", "Bearer " + token))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//    }
//
//    // +mock, GET procedure by id - 400 ERROR "pet not assigned to this procedure"
//    @Test
//    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
//    public void testGetProcedureErrorProcedureForbidden() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{petId}/procedure/{procedureId}", 101, 102)
//                        .header("Authorization", "Bearer " + token))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//    }
//
//    // +mock, ADD new procedure - 201 SUCCESS
//    @Test
//    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
//    public void testAddProcedureSuccess() throws Exception {
//        int beforeCount = procedureDao.getAll().size();
//        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/{petId}/procedure", 102)
//                        .header("Authorization", "Bearer " + token)
//                        .content(objectMapper.valueToTree(newProcedureDto).toString())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isCreated());
//        assertThat(++beforeCount).isEqualTo(procedureDao.getAll().size());
//    }
//
//    // +mock, ADD new procedure - 404 ERROR "pet not found"
//    @Test
//    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
//    public void testAddProcedureErrorPetNotFound() throws Exception {
//        int beforeCount = procedureDao.getAll().size();
//        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/{petId}/procedure", 33)
//                        .header("Authorization", "Bearer " + token)
//                        .content(objectMapper.valueToTree(newProcedureDto).toString())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isNotFound());
//        assertThat(beforeCount).isEqualTo(procedureDao.getAll().size());
//    }
//
//    // +mock, ADD new procedure - 400 ERROR "pet not yours"
//    @Test
//    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
//    public void testAddProcedureErrorPetForbidden() throws Exception {
//        int beforeCount = procedureDao.getAll().size();
//        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/{petId}/procedure", 100)
//                        .header("Authorization", "Bearer " + token)
//                        .content(objectMapper.valueToTree(newProcedureDto).toString())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//        assertThat(beforeCount).isEqualTo(procedureDao.getAll().size());
//    }
//
//    // +mock, UPDATE  procedure - 200 SUCCESS
//    @Test
//    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
//    public void testUpdateProcedureSuccess() throws Exception {
//        int beforeCount = procedureDao.getAll().size();
//        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}/procedure/{id}", 102, 102)
//                        .header("Authorization", "Bearer " + token)
//                        .content(objectMapper.valueToTree(procedureDto3).toString())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//        assertThat(beforeCount).isEqualTo(procedureDao.getAll().size());
//    }
//
//
//    // +mock, UPDATE  procedure - 404 ERROR "pet not found"
//    @Test
//    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
//    public void testUpdateProcedureErrorPetNotFound() throws Exception {
//        int beforeCount = procedureDao.getAll().size();
//        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}/procedure/{id}", 33, 102)
//                        .header("Authorization", "Bearer " + token)
//                        .content(objectMapper.valueToTree(procedureDto3).toString())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isNotFound());
//        assertThat(beforeCount).isEqualTo(procedureDao.getAll().size());
//    }
//
//    // +mock, UPDATE  procedure - 404 ERROR "procedure not found"
//    @Test
//    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
//    public void testUpdateProcedureErrorProcedureNotFound() throws Exception {
//        int beforeCount = procedureDao.getAll().size();
//        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}/procedure/{id}", 102, 33)
//                        .header("Authorization", "Bearer " + token)
//                        .content(objectMapper.valueToTree(procedureDto3).toString())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isNotFound());
//        assertThat(beforeCount).isEqualTo(procedureDao.getAll().size());
//    }
//
//    // +mock, UPDATE  procedure - 400 ERROR "pet not yours"
//    @Test
//    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
//    public void testUpdateProcedureErrorPetForbidden() throws Exception {
//        int beforeCount = procedureDao.getAll().size();
//        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}/procedure/{id}", 100, 100)
//                        .header("Authorization", "Bearer " + token)
//                        .content(objectMapper.valueToTree(procedureDto3).toString())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//        assertThat(beforeCount).isEqualTo(procedureDao.getAll().size());
//    }
//
//    // +mock, UPDATE  procedure - 400 ERROR "pet not assigned to this procedure"
//    @Test
//    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
//    public void testUpdateProcedureErrorProcedureForbidden() throws Exception {
//        int beforeCount = procedureDao.getAll().size();
//        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{petId}/procedure/{id}", 101, 102)
//                        .header("Authorization", "Bearer " + token)
//                        .content(objectMapper.valueToTree(procedureDto3).toString())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//        assertThat(beforeCount).isEqualTo(procedureDao.getAll().size());
//    }
//
//    // +mock, DELETE procedure - 200 SUCCESS
//    @Test
//    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
//    public void testDeleteProcedureSuccess() throws Exception {
//        int beforeCount = procedureDao.getAll().size();
//        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{petId}/procedure/{id}", 102, 102)
//                        .header("Authorization", "Bearer " + token)
//                        .content(objectMapper.valueToTree(procedureDto3).toString())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//        assertThat(--beforeCount).isEqualTo(procedureDao.getAll().size());
//    }
//
//    // +mock, DELETE procedure - 404 ERROR "pet not found"
//    @Test
//    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
//    public void testDeleteProcedureErrorPetNotFound() throws Exception {
//        int beforeCount = procedureDao.getAll().size();
//        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{petId}/procedure/{id}", 33, 102)
//                        .header("Authorization", "Bearer " + token)
//                        .content(objectMapper.valueToTree(procedureDto3).toString())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isNotFound());
//        assertThat(beforeCount).isEqualTo(procedureDao.getAll().size());
//    }
//
//    // +mock, DELETE procedure - 404 ERROR "procedure not found"
//    @Test
//    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
//    public void testDeleteProcedureErrorProcedureNotFound() throws Exception {
//        int beforeCount = procedureDao.getAll().size();
//        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{petId}/procedure/{id}", 102, 33)
//                        .header("Authorization", "Bearer " + token)
//                        .content(objectMapper.valueToTree(procedureDto3).toString())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isNotFound());
//        assertThat(beforeCount).isEqualTo(procedureDao.getAll().size());
//    }
//
//    // +mock, DELETE procedure - 400 ERROR "pet not yours"
//    @Test
//    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
//    public void testDeleteProcedureErrorPetForbidden() throws Exception {
//        int beforeCount = procedureDao.getAll().size();
//        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{petId}/procedure/{id}", 100, 100)
//                        .header("Authorization", "Bearer " + token)
//                        .content(objectMapper.valueToTree(procedureDto3).toString())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//        assertThat(beforeCount).isEqualTo(procedureDao.getAll().size());
//    }
//
//    // +mock, DELETE procedure - 400 ERROR "pet not assigned to this procedure"
//    @Test
//    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml", "/datasets/medicine.yml", "/datasets/procedure.yml", "datasets/reproduction.yml"})
//    public void testDeleteProcedureErrorProcedureForbidden() throws Exception {
//        int beforeCount = procedureDao.getAll().size();
//        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{petId}/procedure/{id}", 101, 102)
//                        .header("Authorization", "Bearer " + token)
//                        .content(objectMapper.valueToTree(procedureDto3).toString())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//        assertThat(beforeCount).isEqualTo(procedureDao.getAll().size());
//    }
//}
