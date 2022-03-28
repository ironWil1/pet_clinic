package com.vet24.web.controllers.notification;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.notification.NotificationDto;
import com.vet24.models.dto.notification.UserNotificationDto;
import com.vet24.models.dto.user.UserInfoDto;
import com.vet24.models.notification.UserNotification;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class UserNotificationControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/user/notification";
    private String token;
    private UserNotificationDto userNotificationDto;

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("user3@gmail.com", "user3");
    }

    @Before
    public void createUserNotificationDto() {
        userNotificationDto = new UserNotificationDto();
        userNotificationDto.setId(5L);
        userNotificationDto.setUser(new UserInfoDto(3L, "user3@gmail.com", "Ivan", "Ivanov"));
        userNotificationDto.setNotification(new NotificationDto(1L, "User Notification Test 1", LocalDate.of(2022, 03, 18), true));
        userNotificationDto.setShow(false);
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/userNotification_controller/user-entities.yml",
            "/datasets/controllers/userNotification_controller/user_notification.yml",
            "/datasets/controllers/userNotification_controller/notification.yml"})
    public void testGetAllNotifications() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(userNotificationDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].id", Is.is(5)))
                .andExpect(jsonPath("$[0].show", Is.is(false)))

                .andExpect(jsonPath("$[0].user.id", Is.is(3)))
                .andExpect(jsonPath("$[0].user.email", Is.is("user3@gmail.com")))
                .andExpect(jsonPath("$[0].user.firstname", Is.is("Ivan")))
                .andExpect(jsonPath("$[0].user.lastname", Is.is("Ivanov")))

                .andExpect(jsonPath("$[0].notification.id", Is.is(1)))
                .andExpect(jsonPath("$[0].notification.content", Is.is("User Notification Test 1")))
                .andExpect(jsonPath("$[0].notification.eventDate", Is.is("2022-03-18")))
                .andExpect(jsonPath("$[0].notification.important", Is.is(true)));
    }

    @Test
    @DataSet(value = {"/datasets/controllers/userNotification_controller/user-entities.yml",
            "/datasets/controllers/userNotification_controller/user_notification.yml",
            "/datasets/controllers/userNotification_controller/notification.yml"}, cleanBefore = true)
    public void testGetUserNotificationById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{notificationId}", 5)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(userNotificationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Is.is(5)))
                .andExpect(jsonPath("$.show", Is.is(false)))

                .andExpect(jsonPath("$.user.id", Is.is(3)))
                .andExpect(jsonPath("$.user.email", Is.is("user3@gmail.com")))
                .andExpect(jsonPath("$.user.firstname", Is.is("Ivan")))
                .andExpect(jsonPath("$.user.lastname", Is.is("Ivanov")))

                .andExpect(jsonPath("$.notification.id", Is.is(1)))
                .andExpect(jsonPath("$.notification.content", Is.is("User Notification Test 1")))
                .andExpect(jsonPath("$.notification.eventDate", Is.is("2022-03-18")))
                .andExpect(jsonPath("$.notification.important", Is.is(true)));
    }

    @Test
    @DataSet(value = {"/datasets/controllers/userNotification_controller/user-entities.yml",
            "/datasets/controllers/userNotification_controller/user_notification.yml",
            "/datasets/controllers/userNotification_controller/notification.yml"}, cleanBefore = true)
    public void UserNotificationNotFoundById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{notificationId}", 7)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(userNotificationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.message", Is.is("UserNotification not found")));
    }

    @Test
    @DataSet(value = {"/datasets/controllers/userNotification_controller/user-entities.yml",
            "/datasets/controllers/userNotification_controller/user_notification.yml",
            "/datasets/controllers/userNotification_controller/notification.yml"}, cleanBefore = true)
    public void testNotificationsStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{notificationId}", 5)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(userNotificationDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());
        UserNotification userNotification = entityManager
                .createQuery("SELECT un FROM UserNotification un WHERE un.id = 5", UserNotification.class)
                .getSingleResult();
        assertEquals(false, userNotification.isShow());
    }

    @Test
    @DataSet(value = {"/datasets/controllers/userNotification_controller/user-entities.yml",
            "/datasets/controllers/userNotification_controller/user_notification.yml",
            "/datasets/controllers/userNotification_controller/notification.yml"}, cleanBefore = true)
    public void testNotificationsStatusNotCorrectId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{notificationId}", 7)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(userNotificationDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.message", Is.is("UserNotification not found")));
    }
}


