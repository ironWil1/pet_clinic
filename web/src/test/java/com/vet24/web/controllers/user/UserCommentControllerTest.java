package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.user.CommentDao;
import com.vet24.models.user.Comment;
import com.vet24.service.user.UserServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@WithUserDetails(value = "user3@gmail.com")
public class UserCommentControllerTest extends ClientControllerTest {

    final String URI = "/api/user/comment/{commentId}";

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserServiceImpl userService;

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/comments.yml"})
    public void likeOrDislikeComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{positive}", 101, true))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Assert.assertEquals(userService.getByKey(3L).getCommentReactions().size(), 1);
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/comments.yml"})
    public void createOrUpdate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI, 101)
                .content("{\n" +
                        "  \"id\": 0,\n" +
                        "  \"content\": \"aaassdd\",\n" +
                        "  \"dateTime\": \"2021-09-28T15:04:12.327Z\",\n" +
                        "  \"likes\": 0,\n" +
                        "  \"dislike\": 0,\n" +
                        "  \"userInfoDto\": {\n" +
                        "    \"id\": 0,\n" +
                        "    \"email\": \"string\",\n" +
                        "    \"firstname\": \"string\",\n" +
                        "    \"lastname\": \"string\"\n" +
                        "  }\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        Comment after = commentDao.getByKey(101L);
        Assert.assertEquals("aaassdd", after.getContent());
        Assert.assertEquals(Long.valueOf(3L), after.getUser().getId());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/comments.yml"})
    public void removeComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI, 101))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(commentDao.isExistByKey(101L)).isEqualTo(false);
    }
}