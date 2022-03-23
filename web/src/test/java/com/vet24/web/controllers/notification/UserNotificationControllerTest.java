package com.vet24.web.controllers.notification;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.notification.UserNotificationDto;
import com.vet24.models.notification.UserNotification;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import javax.persistence.EntityManager;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class UserNotificationControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/user/notification";
    private String token;
    private UserNotificationDto userNotificationDto;

    @Autowired
    EntityManager entityManager;

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("user3@gmail.com", "user3");
    }

    @Before
    public void createUserNotificationDto() {
        userNotificationDto = new UserNotificationDto();
        userNotificationDto.setId(1L);
        userNotificationDto.setContent("User Notification Test 1");
        userNotificationDto.setImportant(true);
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notification/user_notification.yml",
            "/datasets/notification/notification.yml"})
    public void testGetAllNotifications() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(userNotificationDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"id\":1,\"content\":\"User Notification Test 1\",\"important\":true}"));
        assertThat(entityManager.createQuery("FROM UserNotification un " +
                "WHERE un.user.id=100", UserNotification.class).getResultList())
                .isEqualTo("UserNotification Test 1");
    }

    @Test
    @DataSet(value = {"/datasets/user-entities.yml", "/datasets/notification/user_notification.yml",
            "/datasets/notification/notification.yml"}, cleanBefore = true)
    public void testGetUserNotificationById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{notificationId}", 5)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(userNotificationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"id\":1,\"content\":\"User Notification Test 1\",\"important\":true}"));
        assertThat(entityManager.createQuery("FROM UserNotification un " +
                "WHERE un.id=5", UserNotification.class).getSingleResult().getNotification().getContent())
                .isEqualTo("UserNotification Test 1");

    }

    @Test
    @DataSet(value = {"/datasets/user-entities.yml", "/datasets/notification/user_notification.yml",
            "/datasets/notification/notification.yml"}, cleanBefore = true)
    public void testNotificationsStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{notificationId}", 5)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(userNotificationDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(entityManager.createQuery("SELECT un FROM UserNotification un WHERE un.isShow = false", UserNotification.class)
                .getSingleResult().isShow()).isEqualTo(false);
    }

}
