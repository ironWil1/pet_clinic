package com.vet24.web.controllers.notification;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.notification.NotificationDto;
import com.vet24.models.notification.Notification;
import com.vet24.service.notification.NotificationService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;


public class AdminNotificationControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/admin/notification";
    private String token;
    private NotificationDto notificationDto;
    @Autowired
    private NotificationService notificationService;

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
        token = getAccessToken("admin1@email.com", "admin");
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notification/notifications.yml"})
    public void getAllNotifications() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals(4, notificationService.getAll().size());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notification/notifications.yml"})
    public void noNotifications() throws Exception {
        List<Notification> notificationList = notificationService.getAll();
        notificationService.deleteAll(notificationList);

        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertTrue(notificationService.getAll().isEmpty());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notification/notifications.yml"})
    public void getNotificationById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{id}", 101)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals("right get notification by ID", notificationService.getByKey(101L).getContent());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notification/notifications.yml"})
    public void notificationNotFoundById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{id}", 1000)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertFalse(notificationService.isExistByKey(1000L));
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notification/notifications.yml"})
    public void notificationUpdateById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 103)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals("testContent", notificationService.getByKey(103L).getContent());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notification/notifications.yml"})
    public void notificationUpdateWrongId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 1001)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertFalse(notificationService.isExistByKey(1001L));
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notification/notifications.yml"})
    public void notificationDeleteById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 104)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertFalse(notificationService.isExistByKey(104L));
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notification/notifications.yml"})
    public void notificationDeleteWrongId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 1002)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertFalse(notificationService.isExistByKey(1002L));
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notification/notifications.yml"})
    public void notificationCreate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(adminNotification))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals("Тестовое уведомление ADMIN", notificationService.getByKey(1L).getContent());
    }

}
