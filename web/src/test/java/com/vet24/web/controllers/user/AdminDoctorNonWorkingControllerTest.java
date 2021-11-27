package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.user.DoctorNonWorkingDto;
import com.vet24.models.enums.DayOffType;
import com.vet24.models.user.DoctorNonWorking;
import com.vet24.service.user.DoctorNonWorkingService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;

@Slf4j
public class AdminDoctorNonWorkingControllerTest extends ControllerAbstractIntegrationTest {

    final String URI = "/api/admin/doctor_non_working/";
    private String token;
    private DoctorNonWorkingDto doctorNonWorkingDto;
    private DoctorNonWorkingDto doctorNonWorkingDto2;

    @Autowired
    private DoctorNonWorkingService doctorNonWorkingService;

    @Before
    public void createDoctorNonWorkingDto() {
        doctorNonWorkingDto = new DoctorNonWorkingDto();
        doctorNonWorkingDto.setDate(LocalDate.of(2021, 10, 05));
        doctorNonWorkingDto.setType(DayOffType.VACATION);
        doctorNonWorkingDto.setDoctorId(32L);

        doctorNonWorkingDto2 = new DoctorNonWorkingDto();
        doctorNonWorkingDto2.setDate(LocalDate.of(2021, 10, 12));
        doctorNonWorkingDto2.setType(DayOffType.DAY_OFF);
        doctorNonWorkingDto2.setDoctorId(32L);
    }

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("admin1@email.com","admin");
    }

    @Test
    @DataSet(value = {"datasets/doctor-non-working.yml", "datasets/user-entities.yml"}, cleanBefore = true)
    public void updateDNWTest() throws Exception {
        int count = doctorNonWorkingService.getAll().size();
        DoctorNonWorking doctorNonWorking = doctorNonWorkingService.getByKey(101L);
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "{id}", 101)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(doctorNonWorkingDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        Assert.assertFalse(doctorNonWorking.getDate().isEqual(doctorNonWorkingDto.getDate()) &&
                doctorNonWorking.getType().equals(doctorNonWorkingDto.getType()));
        assertThat(count).isEqualTo(doctorNonWorkingService.getAll().size());
    }

    @Test
    @DataSet(value = {"datasets/doctor-non-working.yml", "datasets/user-entities.yml"}, cleanBefore = true)
    public void deleteDNWTest() throws Exception {
        int count = doctorNonWorkingService.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "{id}", 101)
                        .header("Authorization", "Bearer " + token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(--count).isEqualTo(doctorNonWorkingService.getAll().size());
        assertThat(doctorNonWorkingService.getByKey(101L)).isNull();
    }

    @Test
    @DataSet(value = {"datasets/doctor-non-working.yml", "datasets/user-entities.yml"}, cleanBefore = true)
    public void createDNWTest() throws Exception {
        int count = doctorNonWorkingService.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(doctorNonWorkingDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(++count).isEqualTo(doctorNonWorkingService.getAll().size());
    }

    @Test
    @DataSet(value = {"datasets/doctor-non-working.yml", "datasets/user-entities.yml"}, cleanBefore = true)
    public void createDNWIncorrectDataTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(doctorNonWorkingDto2))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    @DataSet(value = {"datasets/doctor-non-working.yml", "datasets/user-entities.yml"}, cleanBefore = true)
    public void updateDNWIncorrectDataTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "{id}", 102)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(doctorNonWorkingDto2))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }
}
