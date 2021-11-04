package com.vet24.web.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.enums.WorkShift;
import com.vet24.models.medicine.DoctorSchedule;
import com.vet24.service.medicine.DoctorScheduleService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WithUserDetails("admin@gmail.com")
public class AdminDoctorScheduleControllerTest extends ControllerAbstractIntegrationTest {
    private final String URI = "http://localhost:8080/api/admin/schedule";

    @Autowired
    private DoctorScheduleService doctorScheduleService;

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/doctor_schedule.yml"})
    public void updateSchedule() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI)
                        .content("{\n" +
                                "  \"id\": 100,\n" +
                                "  \"workShift\": \"SECOND_SHIFT\",\n" +
                                "  \"weekNumber\": 52\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        DoctorSchedule doctorSchedule = doctorScheduleService.getByKey(100L);
        Assert.assertEquals(WorkShift.SECOND_SHIFT, doctorSchedule.getWorkShift());
        Assert.assertEquals((Integer) 52, doctorSchedule.getWeekNumber());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/doctor_schedule.yml"})
    public void removeSchedule() throws Exception {
        assertThat(doctorScheduleService.isExistByKey(101L)).isEqualTo(true);
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 101))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(doctorScheduleService.isExistByKey(101L)).isEqualTo(false);
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/doctor_schedule.yml"})
    public void scheduleDeletedNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 1_000_000))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/doctor_schedule.yml"})
    public void scheduleUpdatedNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI)
                        .content("{\n" +
                                "  \"id\": 100500,\n" +
                                "  \"workShift\": \"SECOND_SHIFT\",\n" +
                                "  \"weekNumber\": 52\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/doctor_schedule.yml"})
    public void scheduleCreateDoctorNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .content("{\n" +
                                "  \"doctorInfo\": {\n" +
                                "    \"id\": 1\n" +
                                "  },\n" +
                                "  \"workShift\": \"FIRST_SHIFT\",\n" +
                                "  \"weekNumber\": 53\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
