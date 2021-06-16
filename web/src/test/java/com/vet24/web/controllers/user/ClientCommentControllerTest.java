package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.vet24.models.user.Client;
import com.vet24.service.user.ClientService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@DBRider
public class ClientCommentControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI_LIKE = "/api/client/{commentId}/{positive}";

    @Autowired
    private ClientService clientService;

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/roles.yml","/datasets/clients.yml","/datasets/doctors.yml", "/datasets/comments.yml"})
    public void shouldBeNotFoundComment() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(URI_LIKE,10000L,false))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/roles.yml","/datasets/clients.yml","/datasets/doctors.yml", "/datasets/comments.yml"})
    public void shouldBeDislikedComment() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(URI_LIKE,1L,false))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Client client = clientService.testGetCurrentClientWithReactions();
        Assert.assertEquals(client.getCommentReactions().size(), 1);
        Assert.assertEquals(client.getCommentReactions().get(0).getPositive(), false  );
    }

    @Test
    @DataSet(cleanBefore = true, value =  {"/datasets/roles.yml","/datasets/clients.yml","/datasets/doctors.yml", "/datasets/comments.yml"})
    public void shouldBeLikedComment() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(URI_LIKE,2L,true))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Client client = clientService.testGetCurrentClientWithReactions();
        Assert.assertEquals(client.getCommentReactions().size(), 1);
        Assert.assertEquals(client.getCommentReactions().get(0).getPositive(), true);
    }
}
