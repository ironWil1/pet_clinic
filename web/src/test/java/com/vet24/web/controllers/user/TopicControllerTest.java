package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.service.user.TopicService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class TopicControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/user/topic";
    private String token;

    @Autowired
    private TopicService topicService;

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("client1@email.com", "client");
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void persistTopicComment() throws Exception {

        final Long topicId = 102L;
        int beforeCount = topicService.getTopicWithCommentsById(topicId).getComments().size();

        mockMvc.perform(MockMvcRequestBuilders.post(
                URI + "/{topicId}/addComment", topicId)
                        .header("Authorization",
                                "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("Some comment string"))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Assert.assertEquals(++beforeCount, topicService.getTopicWithCommentsById(topicId).getComments().size());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void whenManagerTryAddEmptyComment_thenBadRequestException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(
                URI + "/{topicId}/addComment", 102L)
                        .header("Authorization",
                                "Bearer " + getAccessToken("manager1@email.com","manager"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void whenClientTryAddCommentToNotExistsTopic_thenNotFoundException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(
                URI + "/{topicId}/addComment", 7_123L)
                        .header("Authorization",
                                "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("Any comment string"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void whenClientTryAddCommentToClosedTopic_thenForbiddenException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(
                URI + "/{topicId}/addComment", 101L)
                        .header("Authorization",
                                "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("Mockito.anyString()"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

}