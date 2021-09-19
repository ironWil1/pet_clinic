package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.vet24.dao.user.CommentDao;
import com.vet24.models.user.Client;
import com.vet24.service.user.ClientService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@WithUserDetails(value = "user3@gmail.com")
public class ClientReviewControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/client/doctor";

    @Autowired
    private ClientService clientService;

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/clients.yml", "/datasets/doctors.yml", "/datasets/comments.yml"})
    public void shouldBeNotFoundComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/{commentId}/{positive}", 10000L, false))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/clients.yml", "/datasets/doctors.yml", "/datasets/comments.yml"})
    public void shouldBeDislikedComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/{commentId}/{positive}", 101L, false))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(
                        (result) -> {
                            Client client = clientService.getCurrentClientWithReactions();
                            Assert.assertEquals(client.getCommentReactions().size(), 1);
                            Assert.assertEquals(client.getCommentReactions().get(0).getPositive(), false);
                        }
                );
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/clients.yml", "/datasets/doctors.yml", "/datasets/comments.yml"})
    public void shouldBeLikedComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/{commentId}/{positive}", 102L, true))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(
                        (result) -> {
                            Client client = clientService.getCurrentClientWithReactions();
                            Assert.assertEquals(client.getCommentReactions().size(), 1);
                            Assert.assertEquals(client.getCommentReactions().get(0).getPositive(), true);
                        }
                );
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/clients.yml", "/datasets/comments.yml"})
    @ExpectedDataSet(value = {"/datasets/expected/comments.yml"})
    public void shouldBeUpdatedComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{commentId}/review/update", 102)
                .content("This doctor is very bad!!")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("This doctor is very bad!!"));
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/clients.yml", "/datasets/comments.yml"})
    public void updatedCommentNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{commentId}/review/update", 50000)
                .content("This doctor is very GOOOD!!")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/clients.yml", "/datasets/comments.yml"})
    public void shouldBeDeleteComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{commentId}/review/delete", 103))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/clients.yml", "/datasets/comments.yml"})
    public void deletedCommentNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{commentId}/review/delete", 50000))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }


}
