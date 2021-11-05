package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.user.DoctorNonWorkingDto;
import com.vet24.models.enums.DayOffType;
import com.vet24.service.user.DoctorNonWorkingService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import lombok.extern.slf4j.Slf4j;
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
@WithUserDetails("admin@gmail.com")
public class AdminDoctorNonWorkingControllerTest extends ControllerAbstractIntegrationTest {

    final String URI = "http://localhost:8080/api/admin/doctor_non_working/";
    private DoctorNonWorkingDto doctorNonWorkingDto;
    private DoctorNonWorkingDto doctorNonWorkingDtoTest;
    @Autowired
    private DoctorNonWorkingService doctorNonWorkingService;

    @Before
    public void createDoctorNonWorkingDto() {
        doctorNonWorkingDto = new DoctorNonWorkingDto();
        doctorNonWorkingDto.setDate(LocalDate.of(2016, 12, 11));
        doctorNonWorkingDto.setType(DayOffType.DAY_OFF);
        doctorNonWorkingDto.setDoctorId(31L);
    }

    @Test
    @DataSet(value = {"datasets/doctor-non-working.yml", "datasets/user-entities.yml"}, cleanBefore = true)
    public void updateDNWTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "{id}", 101)
                .content(objectMapper.writeValueAsString(doctorNonWorkingDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(doctorNonWorkingDto).isNotEqualTo(doctorNonWorkingService.getByKey(2l));
    }

    @Test
    @DataSet(value = {"datasets/doctor-non-working.yml", "datasets/user-entities.yml"}, cleanBefore = true)
    public void deleteDNWTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "{id}", 101))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(doctorNonWorkingService.getByKey(2l)).isNull();
    }

    @Test
    @DataSet(value = {"datasets/doctor-non-working.yml", "datasets/user-entities.yml"}, cleanBefore = true)
    public void createDNWTest() throws Exception {
        int count = doctorNonWorkingService.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                .content(objectMapper.writeValueAsString(doctorNonWorkingDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(++count).isEqualTo(doctorNonWorkingService.getAll().size());
    }
}
