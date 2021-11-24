package com.vet24.web.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.user.DoctorScheduleDto;
import com.vet24.models.enums.WorkShift;
import com.vet24.models.medicine.DoctorSchedule;
import com.vet24.service.medicine.DoctorScheduleService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WithUserDetails("admin@gmail.com")
public class AdminDoctorScheduleControllerTest extends ControllerAbstractIntegrationTest {
    private final String URI = "http://localhost:8080/api/admin/schedule";
    private DoctorScheduleDto updateSuccessSchedule;
    private DoctorScheduleDto updateNotFoundSchedule;
    private DoctorScheduleDto createDoctorNotFoundSchedule;
    private DoctorScheduleDto createDoctorIsBusySchedule;
    private DoctorScheduleDto createDoctorSuccess;

    @Autowired
    private DoctorScheduleService doctorScheduleService;

    @Before
    public void createDoctorScheduleDto() {
        updateSuccessSchedule = new DoctorScheduleDto();
        updateSuccessSchedule.setId(100L);
        updateSuccessSchedule.setWorkShift(WorkShift.SECOND_SHIFT);

        updateNotFoundSchedule = new DoctorScheduleDto();
        updateNotFoundSchedule.setId(100500L);
        updateNotFoundSchedule.setWorkShift(WorkShift.SECOND_SHIFT);

        createDoctorNotFoundSchedule = new DoctorScheduleDto();
        createDoctorNotFoundSchedule.setDoctorId(100500L);
        createDoctorNotFoundSchedule.setWorkShift(WorkShift.SECOND_SHIFT);
        createDoctorNotFoundSchedule.setWeekNumber(15);

        createDoctorIsBusySchedule = new DoctorScheduleDto();
        createDoctorIsBusySchedule.setDoctorId(103L);
        createDoctorIsBusySchedule.setWorkShift(WorkShift.SECOND_SHIFT);
        createDoctorIsBusySchedule.setWeekNumber(30);

        createDoctorSuccess = new DoctorScheduleDto();
        createDoctorSuccess.setDoctorId(103L);
        createDoctorSuccess.setWorkShift(WorkShift.SECOND_SHIFT);
        createDoctorSuccess.setWeekNumber(1);
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/doctor_schedule.yml"})
    public void updateSchedule() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI)
                        .content(objectMapper.valueToTree(updateSuccessSchedule).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        DoctorSchedule doctorSchedule = doctorScheduleService.getByKey(100L);
        Assert.assertEquals(WorkShift.SECOND_SHIFT, doctorSchedule.getWorkShift());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/doctor_schedule.yml"})
    public void removeSchedule() throws Exception {
        assertTrue(doctorScheduleService.isExistByKey(101L));
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 101))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertFalse(doctorScheduleService.isExistByKey(101L));
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
                        .content(objectMapper.valueToTree(updateNotFoundSchedule).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/doctor_schedule.yml"})
    public void scheduleCreateDoctorNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .content(objectMapper.valueToTree(createDoctorNotFoundSchedule).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/doctor_schedule.yml"})
    public void scheduleCreateDoctorIsBusy() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .content(objectMapper.valueToTree(createDoctorIsBusySchedule).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/doctor_schedule.yml"})
    public void scheduleCreateSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .content(objectMapper.valueToTree(createDoctorSuccess).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
