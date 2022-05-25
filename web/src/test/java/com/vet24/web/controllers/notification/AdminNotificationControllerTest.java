package com.vet24.web.controllers.notification;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.notification.NotificationDto;
import com.vet24.models.notification.Notification;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


public class AdminNotificationControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/admin/notification";
    private String token;
    private NotificationDto notificationDto;

    private Notification adminNotification = new Notification("Тестовое уведомление ADMIN",
            LocalDate.of(2022, 3, 26),
            true);

    @Before
    public void createNotificationDto() {
        notificationDto = new NotificationDto();
        notificationDto.setId(103L);
        notificationDto.setContent("testContent");
        notificationDto.setEventDate(LocalDate.of(2022, 3, 26));
        notificationDto.setImportant(true);
    }

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("admin1@email.com", "admin");
    }


    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/notification_controller/user-entities.yml", "/datasets/controllers/notification_controller/notifications.yml"})
    public void getAllNotifications() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(notificationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].id", Is.is(101)))
                .andExpect(jsonPath("$[0].content", Is.is("right get notification by ID")))
                .andExpect(jsonPath("$[0].eventDate", Is.is("2022-03-03")))
                .andExpect(jsonPath("$[0].important", Is.is(true)))

                .andExpect(jsonPath("$[1].id", Is.is(102)))
                .andExpect(jsonPath("$[1].content", Is.is("right notification")))
                .andExpect(jsonPath("$[1].eventDate", Is.is("2022-03-04")))
                .andExpect(jsonPath("$[1].important", Is.is(true)))

                .andExpect(jsonPath("$[2].id", Is.is(103)))
                .andExpect(jsonPath("$[2].content", Is.is("right notification")))
                .andExpect(jsonPath("$[2].eventDate", Is.is("2022-03-06")))
                .andExpect(jsonPath("$[2].important", Is.is(true)))

                .andExpect(jsonPath("$[3].id", Is.is(104)))
                .andExpect(jsonPath("$[3].content", Is.is("wrong notification")))
                .andExpect(jsonPath("$[3].eventDate", Is.is("2022-03-06")))
                .andExpect(jsonPath("$[3].important", Is.is(true)));
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/notification_controller/user-entities.yml"})
    public void noNotifications() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(notificationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Matchers.empty()));
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/notification_controller/user-entities.yml", "/datasets/controllers/notification_controller/notifications.yml"})
    public void getNotificationById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{id}", 101)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(notificationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Is.is(101)))
                .andExpect(jsonPath("$.content", Is.is("right get notification by ID")))
                .andExpect(jsonPath("$.eventDate", Is.is("2022-03-03")))
                .andExpect(jsonPath("$.important", Is.is(true)));
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/notification_controller/user-entities.yml", "/datasets/controllers/notification_controller/notifications.yml"})
    public void notificationNotFoundById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{id}", 10)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(notificationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.message", Is.is("Notification not found")));
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/notification_controller/user-entities.yml", "/datasets/controllers/notification_controller/notifications.yml"})
    public void notificationUpdateById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 103)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(notificationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Is.is(103)))
                .andExpect(jsonPath("$.content", Is.is("testContent")))
                .andExpect(jsonPath("$.eventDate", Is.is("2022-03-26")))
                .andExpect(jsonPath("$.important", Is.is(true)));

        Notification notification = entityManager
                .createQuery("SELECT n from Notification n WHERE n.id = 103", Notification.class)
                .getSingleResult();
        assertEquals("testContent", notification.getContent());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/notification_controller/user-entities.yml", "/datasets/controllers/notification_controller/notifications.yml"})
    public void notificationUpdateWrongId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 11)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(notificationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.message", Is.is("Notification not found")));

        assertEquals(Long.valueOf(4),
                entityManager.createQuery("SELECT COUNT(n) from Notification n", Long.class).getSingleResult());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/notification_controller/user-entities.yml", "/datasets/controllers/notification_controller/notifications.yml"})
    public void notificationDeleteById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 104)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(notificationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertFalse("Проверка удаления уведомления из БД",
                entityManager.createQuery("SELECT COUNT(n) > 0 FROM Notification n WHERE n.id = 104", Boolean.class).getSingleResult());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/notification_controller/user-entities.yml", "/datasets/controllers/notification_controller/notifications.yml"})
    public void notificationDeleteWrongId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 12)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(notificationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.message", Is.is("Notification not found")));
        assertEquals(Long.valueOf(4),
                entityManager.createQuery("SELECT COUNT(n) FROM Notification n", Long.class).getSingleResult());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/notification_controller/user-entities.yml", "/datasets/controllers/notification_controller/notifications.yml"})
    public void notificationCreate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(adminNotification))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.content", Is.is(adminNotification.getContent())))
                .andExpect(jsonPath("$.eventDate", Is.is(adminNotification.getEventDate().toString())))
                .andExpect(jsonPath("$.important", Is.is(adminNotification.isImportant())));
        //TODO
//                .andExpect(content().string(
//                        "{\"id\":1,\"content\":\"Тестовое уведомление ADMIN\"," +
//                                "\"eventDate\":\"2022-03-26\",\"important\":true}"));
//        Notification notification = entityManager
//                .createQuery("SELECT n from Notification n WHERE n.id = 1", Notification.class)
//                .getSingleResult();
        assertTrue("Проверка наличия нового уведомления в БД",
                entityManager.createQuery("SELECT COUNT(id) > 0 FROM Notification WHERE " +
                                "content = :content AND eventDate = :eventDate AND isImportant = :isImportant", Boolean.class)
                        .setParameter("content", adminNotification.getContent())
                        .setParameter("eventDate", adminNotification.getEventDate())
                        .setParameter("isImportant", adminNotification.isImportant())
                        .getSingleResult());
    }
}
