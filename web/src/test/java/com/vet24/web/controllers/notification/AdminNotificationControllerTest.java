package com.vet24.web.controllers.notification;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.notification.NotificationDto;
import com.vet24.models.notification.Notification;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


public class AdminNotificationControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/admin/notification";
    private String token;
    private NotificationDto notificationDto;
    @Autowired
    EntityManager entityManager;

    Notification adminNotification = new Notification(
            "Тестовое уведомление ADMIN", LocalDate.of(2022,3,26), true);

    @Before
    public void createNotificationDto() {
        notificationDto = new NotificationDto();
        notificationDto.setId(103L);
        notificationDto.setContent("testContent");
        notificationDto.setEventDate(LocalDate.of(2022,3,26));
        notificationDto.setImportant(true);
    }

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("admin1@email.com", "admin");
    }


    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notification/notifications.yml"})
    public void getAllNotifications() throws Exception {
        List<Notification> notificationList = entityManager
                .createQuery("SELECT n from Notification n", Notification.class)
                .getResultList();
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("[" +
                        "{\"id\":101,\"content\":\"right get notification by ID\",\"eventDate\":\"2022-03-03\",\"important\":true}," +
                        "{\"id\":102,\"content\":\"right notification\",\"eventDate\":\"2022-03-04\",\"important\":true}," +
                        "{\"id\":103,\"content\":\"right notification\",\"eventDate\":\"2022-03-06\",\"important\":true}," +
                        "{\"id\":104,\"content\":\"wrong notification\",\"eventDate\":\"2022-03-06\",\"important\":true}]"
                        ));
        Notification notification = entityManager
                .createQuery("SELECT n from Notification n WHERE n.id = 103", Notification.class)
                .getSingleResult();
        assertEquals("right notification", notification.getContent());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml"})
    public void noNotifications() throws Exception {
        List<Notification> notificationList = entityManager
                .createQuery("SELECT n from Notification n", Notification.class)
                .getResultList();
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("[]"));
        assertEquals("[]", entityManager.createQuery("SELECT n from Notification n").getResultList().toString());

    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notification/notifications.yml"})
    public void getNotificationById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{id}", 101)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(
                        "{\"id\":101,\"content\":\"right get notification by ID\"," +
                                "\"eventDate\":\"2022-03-03\",\"important\":true}"));
        Notification notification = entityManager
                .createQuery("SELECT n from Notification n WHERE n.id = 101", Notification.class)
                .getSingleResult();
        assertEquals("right get notification by ID", notification.getContent());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notification/notifications.yml"})
    public void notificationNotFoundById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{id}", 10)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(content().string("{\"message\":\"Notification not found\"}"));
        assertEquals("[]",
                entityManager.createQuery("SELECT n from Notification n WHERE n.id = 10").getResultList().toString());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notification/notifications.yml"})
    public void notificationUpdateById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 103)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(
                        "{\"id\":103,\"content\":\"testContent\",\"eventDate\":\"2022-03-26\",\"important\":true}"
                ));
        Notification notification = entityManager
                .createQuery("SELECT n from Notification n WHERE n.id = 103", Notification.class)
                .getSingleResult();
        assertEquals("testContent", notification.getContent());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notification/notifications.yml"})
    public void notificationUpdateWrongId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 11)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(content().string("{\"message\":\"Notification not found\"}"));
        assertEquals("[]",
                entityManager.createQuery("SELECT n from Notification n WHERE n.id = 11").getResultList().toString());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notification/notifications.yml"})
    public void notificationDeleteById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 104)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(""));
        assertEquals("[]",
                entityManager.createQuery("SELECT n from Notification n WHERE n.id = 104").getResultList().toString());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notification/notifications.yml"})
    public void notificationDeleteWrongId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 12)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(notificationDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(content().string("{\"message\":\"Notification not found\"}"));
        assertEquals("[]",
                entityManager.createQuery("SELECT n from Notification n WHERE n.id = 12").getResultList().toString());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notification/notifications.yml"})
    public void notificationCreate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(adminNotification))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(
                        "{\"id\":1,\"content\":\"Тестовое уведомление ADMIN\"," +
                                "\"eventDate\":\"2022-03-26\",\"important\":true}"));
        Notification notification = entityManager
                .createQuery("SELECT n from Notification n WHERE n.id = 1", Notification.class)
                .getSingleResult();
        assertEquals("Тестовое уведомление ADMIN", notification.getContent());
    }

}
