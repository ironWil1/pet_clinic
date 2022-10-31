package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.user.CommentDao;
import com.vet24.service.user.DoctorReviewService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ClientReviewControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/client/doctor";
    private String token;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private DoctorReviewService doctorReviewService;

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("user3@gmail.com","user3");
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/clients.yml", "/datasets/doctors.yml", "/datasets/comments.yml"})
    public void shouldBeNotFoundComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/{commentId}/{positive}", 10000L, false)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/clients.yml", "/datasets/comments.yml","/datasets/doctor-review.yml", "/datasets/doctors.yml"})
    public void shouldBeUpdatedComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{doctorId}/review", 33)
                        .header("Authorization", "Bearer " + token)
                        .content("This doctor is very bad!!")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(doctorReviewService.getByDoctorAndClientId(33,3).getComment().getContent()).isEqualTo("This doctor is very bad!!");
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/clients.yml", "/datasets/comments.yml","/datasets/doctor-review.yml", "/datasets/doctors.yml"})
    public void updatedCommentNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{doctorId}/review", 50000)
                        .header("Authorization", "Bearer " + token)
                        .content("This doctor is very GOOOD!!")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/clients.yml", "/datasets/comments.yml", "/datasets/doctor-review.yml", "/datasets/doctors.yml"})
    public void shouldBeDeleteComment() throws Exception {
        int beforeCount = commentDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{doctorId}/review", 33)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(--beforeCount).isEqualTo(commentDao.getAll().size());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/clients.yml", "/datasets/comments.yml","/datasets/doctor-review.yml", "/datasets/doctors.yml"})
    public void deletedCommentNotFound() throws Exception {
        int beforeCount = commentDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{doctorId}/review", 50000)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThat(beforeCount).isEqualTo(commentDao.getAll().size());
    }
}
