package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.user.CommentDao;
import com.vet24.models.dto.user.CommentDto;
import com.vet24.models.dto.user.UserInfoDto;
import com.vet24.models.user.Comment;
import com.vet24.service.user.UserServiceImpl;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

public class UserCommentControllerTest extends ControllerAbstractIntegrationTest {

    final String URI = "/api/user/comment/{commentId}";
    private String token;
    private static CommentDto commentDtoUpdateNotYour;
    private static CommentDto commentDtoUpdate;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserServiceImpl userService;

    @BeforeClass
    public static void createTopicDto() {
        UserInfoDto userInfoDto = new UserInfoDto(0L, "string", "string", "String");
        commentDtoUpdateNotYour = new CommentDto();
        commentDtoUpdateNotYour.setId(104L);
        commentDtoUpdateNotYour.setContent("Текст которого не должно быть в content");
        commentDtoUpdateNotYour.setDateTime(LocalDateTime.of(2022, 1, 8, 14, 20, 0));
        commentDtoUpdateNotYour.setUserInfoDto(userInfoDto);
        commentDtoUpdateNotYour.setLikes(0);
        commentDtoUpdateNotYour.setDislike(0);

        commentDtoUpdate = new CommentDto();
        commentDtoUpdate.setId(101L);
        commentDtoUpdate.setContent("Новый комментарий");
        commentDtoUpdate.setDateTime(LocalDateTime.of(2022, 2, 2, 12, 20, 0));
        commentDtoUpdate.setUserInfoDto(userInfoDto);
        commentDtoUpdate.setLikes(0);
        commentDtoUpdate.setDislike(0);
    }

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("user3@gmail.com","user3");
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/comments.yml"})
    public void likeOrDislikeComment() throws Exception {
        assertTrue(userService
                .getWithAllCommentReactions("user3@gmail.com")
                .getCommentReactions().isEmpty());
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{positive}", 101, true)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Assert.assertEquals(1, userService
                .getWithAllCommentReactions("user3@gmail.com")
                .getCommentReactions().size());
        Assert.assertEquals(true, userService
                .getWithAllCommentReactions("user3@gmail.com")
                .getCommentReactions().get(0).getPositive());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/comments.yml"})
    public void updateComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI, 101)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(commentDtoUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Comment after = commentDao.getByKey(101L);
        Assert.assertEquals("Новый комментарий", after.getContent());
        Assert.assertEquals(Long.valueOf(3L), after.getUser().getId());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/comments.yml"})
    public void updateNotYourComment() throws Exception {
        Comment afterComment = entityManager.find(Comment.class, 104L);
        mockMvc.perform(MockMvcRequestBuilders.put(URI, 104)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(commentDtoUpdateNotYour))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        Assert.assertNotEquals("Текст которого не должно быть в content", afterComment.getContent());
        Assert.assertNotEquals(Long.valueOf(3L), afterComment.getUser().getId());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/comments.yml"})
    public void removeComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI, 101)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertFalse(commentDao.isExistByKey(101L));
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/comments.yml"})
    public void removeNotYoursComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI, 104)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        assertNotNull(entityManager.find(Comment.class, 104L));
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/comments.yml"})
    public void shouldNotFoundResponse() throws Exception {
        assertFalse(commentDao.isExistByKey(245L));
        mockMvc.perform(MockMvcRequestBuilders.delete(URI, 245)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        assertFalse(commentDao.isExistByKey(350L));
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{positive}", 350, false)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}