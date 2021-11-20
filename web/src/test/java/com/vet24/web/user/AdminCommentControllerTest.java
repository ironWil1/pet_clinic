package com.vet24.web.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.user.CommentDto;
import com.vet24.models.dto.user.UserInfoDto;
import com.vet24.service.user.CommentService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@WithUserDetails("admin@gmail.com")
public class AdminCommentControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "http://localhost:8080/api/admin/comment";
    private CommentDto commentDto;
    @Autowired
    private CommentService commentService;

    @Before
    public void createCommentDto() {
        commentDto = new CommentDto();
        commentDto.setId(101L);
        commentDto.setUserInfoDto(new UserInfoDto());
        commentDto.setContent("testComment");
        commentDto.setDateTime(LocalDateTime.now());
        commentDto.setLikes(1);
        commentDto.setDislike(0);
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/comments.yml"})
    public void commentUpdatedNotFound() throws Exception {

        String token = this.getAccessToken("admin@gmail.com","admin");

        commentDto.setContent("updatedTestComment");
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 1_000_000)
                        .content(objectMapper.valueToTree(commentDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/comments.yml"})
    public void commentDeletedNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 1_000_000))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/comments.yml"})
    public void commentUpdated() throws Exception {
        String token = getAccessToken("admin@gmail.com","admin");
        String tempComment = "right comment";
        commentDto.setContent("updatedRightComment");
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 103)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(commentDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(tempComment).isNotEqualTo(commentService.getByKey(103L).getContent());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/comments.yml"})
    public void commentDeleted() throws Exception {
        int sizeBefore = commentService.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 101))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(--sizeBefore).isEqualTo(commentService.getAll().size());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml","/datasets/comments.yml", "/datasets/doctor-review.yml"})
    public void commentNotDeletedBecauseConstraints() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 102))
                .andExpect(MockMvcResultMatchers.status().isIAmATeapot());
    }
}
