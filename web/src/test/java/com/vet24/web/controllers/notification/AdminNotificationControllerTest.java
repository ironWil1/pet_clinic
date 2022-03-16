package com.vet24.web.controllers.notification;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.notification.NotificationDto;
import com.vet24.models.notification.Notification;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

public class AdminNotificationControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/admin/notification";
    private String token;
    private NotificationDto notificationDto;

    Notification adminNotification = new Notification("Тестовое уведомление ADMIN", LocalDate.now().plusDays(7), true);

    @Before
    public void createNotificationDto() {
        notificationDto = new NotificationDto();
        notificationDto.setId(103L);
        notificationDto.setContent("testContent");
        notificationDto.setEventDate(LocalDate.now().plusDays(7));
        notificationDto.setImportant(true);
    }
    @Before
    public void setToken() throws Exception {
        token = getAccessToken("admin1@email.com","admin");
//        token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbjFAZW1haWwuY29tIiwiaWF0IjoxNjQ3MDEwMDA1LCJleHAiOjE2NDkwMTAwMDV9.q5a7nD013KmaF41Gk0qyX29tC6tuBKyo2pcmJQTN93b-R7_F0G1DAy4CSWBP8Ywl4MmTwo2SYBdAJlpE1L-TFw";
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notifications.yml"})
    public void getAllNotifications() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notifications.yml"})
    public void noNotifications() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(null).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notifications.yml"})
    public void getNotificationById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{id}", 101)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notifications.yml"})
    public void notificationNotFoundById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{id}", 1000)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notifications.yml"})
    public void notificationUpdateById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 103)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notifications.yml"})
    public void notificationUpdateWrongId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 1000)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notifications.yml"})
    public void notificationDeleteById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 104)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notifications.yml"})
    public void notificationDeleteWrongId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 1000)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notifications.yml"})
    public void notificationCreate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(adminNotification))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }



}
