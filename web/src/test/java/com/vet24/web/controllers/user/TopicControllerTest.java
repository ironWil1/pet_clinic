package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.service.user.TopicService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class TopicControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/user/topic";

    @Autowired
    private TopicService topicService;

    @Test
    @WithUserDetails("client1@email.com")
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void persistTopicComment() throws Exception {

        final Long topicId = 102L;
        int beforeCount = topicService.getTopicWithCommentsById(topicId).getComments().size();

        mockMvc.perform(MockMvcRequestBuilders.post(
                URI + "/{topicId}/addComment", topicId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("Some comment string"))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Assert.assertEquals(++beforeCount, topicService.getTopicWithCommentsById(topicId).getComments().size());
    }

    @Test
    @WithUserDetails("manager@gmail.com")
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void whenManagerTryAddEmptyComment_thenBadRequestException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(
                URI + "/{topicId}/addComment", 102L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithUserDetails("client1@email.com")
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void whenClientTryAddCommentToNotExistsTopic_thenNotFoundException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(
                URI + "/{topicId}/addComment", 7_123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("Any comment string"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithUserDetails("client1@email.com")
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void whenClientTryAddCommentToClosedTopic_thenForbiddenException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(
                URI + "/{topicId}/addComment", 101L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("Mockito.anyString()"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

}