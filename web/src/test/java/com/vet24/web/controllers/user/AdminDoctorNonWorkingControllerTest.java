package com.vet24.web.controllers.user;


import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.user.DoctorDto;
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
@WithUserDetails(value = "admin1@email.com")
public class AdminDoctorNonWorkingControllerTest extends ControllerAbstractIntegrationTest {

    private DoctorNonWorkingDto doctorNonWorkingDto;
    private DoctorNonWorkingDto doctorNonWorkingDtoTest;
    final String URI = "http://localhost:8080/api/admin/doctor_non_working/";

    @Autowired
    private DoctorNonWorkingService doctorNonWorkingService;

    @Before
    public void createDoctorNonWorkingDto(){
        doctorNonWorkingDto = new DoctorNonWorkingDto();
        doctorNonWorkingDto.setDate(LocalDate.of(2016,12,11));
        doctorNonWorkingDto.setType(DayOffType.DAY_OFF);
        doctorNonWorkingDto.setDoctorDto(new DoctorDto(31L,"doc@email.com","doctorTest","testovich"));
    }
    @Before
    public void createDoctorNonWorkingDtoTest() throws Exception {
        doctorNonWorkingDtoTest = new DoctorNonWorkingDto();
        doctorNonWorkingDtoTest.setDate(LocalDate.of(2015, 12, 11));
        doctorNonWorkingDtoTest.setType(DayOffType.DAY_OFF);
        doctorNonWorkingDtoTest.setDoctorDto(new DoctorDto(31L, "doc@email.com", "doctorTest", "testovich"));
    }

    @Test
    @DataSet( value = {"datasets/doctor-non-working.yml"})
    public void updateDNWTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.put(URI+"{id}",2)
        .content(objectMapper.valueToTree(doctorNonWorkingDto).toString())
        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(doctorNonWorkingDtoTest).isNotEqualTo(doctorNonWorkingService.getByKey(2l));
    }


    @Test
    @DataSet( value = {"datasets/doctor-non-working.yml"})
    public void deleteDNWTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete(URI+"{id}",2))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(doctorNonWorkingService.getByKey(2l)).isNull();
    }

    @Test
    @DataSet( value = {"datasets/doctor-non-working.yml"})
    public void createDNWTest() throws Exception{
        int count = doctorNonWorkingService.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                .content(objectMapper.valueToTree(doctorNonWorkingDto).toString())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(++count).isEqualTo(doctorNonWorkingService.getAll().size());
    }

}
