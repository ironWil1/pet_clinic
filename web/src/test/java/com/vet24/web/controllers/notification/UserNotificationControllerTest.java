package com.vet24.web.controllers.notification;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.notification.UserNotificationDao;
import com.vet24.models.dto.notification.UserNotificationDto;
import com.vet24.models.notification.UserNotification;
import com.vet24.service.notification.UserNotificationService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserNotificationControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/user/notification";
    private String token;
    private UserNotificationDto userNotificationDto;
    private List<UserNotificationDto> userNotificationDtoList;

    @Autowired
    UserNotificationService userNotificationService;

    @Autowired
    UserNotificationDao userNotificationDao;

    @Before
    public void createUserNotificationDto() {
        userNotificationDto = new UserNotificationDto();
        userNotificationDto.setId(5L);
        userNotificationDto.setContent("User Notification Test 1");
        userNotificationDto.setImportant(true);
    }

    @Before
    public void createUserNotificationDtoList() {
        UserNotificationDto userNotificationDto1 = new UserNotificationDto();
        userNotificationDto1.setId(9L);
        userNotificationDto1.setContent("User Notification Test 3");
        userNotificationDto1.setImportant(true);

        userNotificationDtoList = new ArrayList<>();
        userNotificationDtoList.add(userNotificationDto1);
    }

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("user3@gmail.com","user3");
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/notification/user_notification.yml", "/datasets/notification/notification.yml"})
    public void testGetAllNotifications() throws Exception{
        List<UserNotification> userNotificationNew = userNotificationService.getAllUserNotificationByUserId(102L);
        int count = userNotificationService.getAllUserNotificationByUserId(102L).size();
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(userNotificationDtoList))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(MockMvcResultMatchers.status().isOk());
         assertThat(userNotificationDtoList).isEqualTo(userNotificationNew);
        assertThat(count).isEqualTo(userNotificationDtoList.size());
    }

    @Test
    @DataSet(value = {"/datasets/user-entities.yml", "/datasets/notification/user_notification.yml", "/datasets/notification/notification.yml"}, cleanBefore = true)
    public void testGetUserNotificationById() throws Exception{
                mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{notificationId}", 5)
                                .header("Authorization", "Bearer " + token)
                                .content(objectMapper.writeValueAsString(userNotificationDto))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(userNotificationDto).isEqualTo(userNotificationService.getByKey(5L));
    }

    @Test
    @DataSet(value = {"/datasets/user-entities.yml", "/datasets/notification/user_notification.yml", "/datasets/notification/notification.yml"}, cleanBefore = true)
    public void testNotificationsStatus() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{notificationId}", 5)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(userNotificationDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(userNotificationService.getByKey(5L).isShow()).isEqualTo(false);
    }

}
