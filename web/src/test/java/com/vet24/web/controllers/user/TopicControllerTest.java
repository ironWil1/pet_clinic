package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.user.Topic;
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
        Topic topic = topicService.getByKey(topicId);
        int beforeCount = topic.getComments().size();
        mockMvc.perform(MockMvcRequestBuilders.post(
                URI + "/{topicId}/addComment", topicId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("Какой то коммент"/*objectMapper.writeValueAsString(commentDto)*//*objectMapper.valueToTree(commentDto).toString()*/))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        Assert.assertEquals(++beforeCount, topicService.getByKey(topicId).getComments().size());
    }
}