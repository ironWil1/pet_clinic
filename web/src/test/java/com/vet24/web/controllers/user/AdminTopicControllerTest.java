package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.user.TopicDao;
import com.vet24.models.dto.user.CommentDto;
import com.vet24.models.dto.user.TopicDto;
import com.vet24.models.dto.user.UserInfoDto;
import com.vet24.models.mappers.user.TopicMapper;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AdminTopicControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    AdminTopicController adminTopicController;
    @Autowired
    TopicDao topicDao;
    @Autowired
    TopicMapper topicMapper;

    final String URI = "http://localhost:8090/api/admin/topic";
    private String token;

    static TopicDto topicDtoClosed;
    static TopicDto topicDtoOpen;
    static TopicDto topicDtoUpdate;
    static TopicDto topicDtoNotFound;
    static UserInfoDto userInfoDto;
    static CommentDto commentDto;
    static List<CommentDto> commentDtoList = new ArrayList<>();

    @BeforeClass
    public static void createTopicDto() {
        userInfoDto = new UserInfoDto(3L, "user3@gmail.com", "Ivan", "Ivanov");
        commentDto = new CommentDto();
        commentDto.setId(101L);
        commentDto.setContent("right  comment");
        commentDto.setDateTime(LocalDateTime.of(2021, 6, 8, 14, 20, 00));
        commentDto.setUserInfoDto(userInfoDto);
        commentDto.setLikes(0);
        commentDto.setDislike(0);
        commentDtoList.add(commentDto);
        topicDtoOpen = new TopicDto(101L, "Почему Земля круглая?", "Какой то контент"
                , LocalDateTime.of(2021, 2, 7, 22, 00, 00)
                , LocalDateTime.of(2021, 2, 7, 22, 00, 00)
                , userInfoDto, commentDtoList);
        topicDtoClosed = new TopicDto(100L, "Какой сегодня день?", "Что то понаписал"
                , LocalDateTime.of(2021, 1, 2, 00, 00, 00)
                , LocalDateTime.of(2021, 6, 8, 14, 20, 00)
                , userInfoDto, commentDtoList);
        topicDtoNotFound = new TopicDto(100584L, "Какой сегодня день?", "Что то понаписал"
                , LocalDateTime.of(2021, 1, 2, 00, 00, 00)
                , LocalDateTime.of(2021, 6, 8, 14, 20, 00)
                , userInfoDto, commentDtoList);
    }

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("admin1@email.com","admin");
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void testDeleteTopicSuccess() throws Exception {
        int beforeCount = topicDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{topicId}", 100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(topicDtoClosed).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(--beforeCount).isEqualTo(topicDao.getAll().size());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void testDeleteTopicNotFound() throws Exception {
        int beforeCount = topicDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{topicId}", 33)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(topicDtoClosed).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(topicDao.getAll().size());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void testCloseTopicSuccess() throws Exception {
        int beforeCount = topicDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{topicId}/close", 100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(topicDtoOpen).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(beforeCount).isEqualTo(topicDao.getAll().size());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void testCloseTopicNotFound() throws Exception {
        int beforeCount = topicDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{topicId}/close", 108562)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(topicDtoClosed).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(topicDao.getAll().size());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void testOpenTopicSuccess() throws Exception {
        int beforeCount = topicDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{topicId}/open", 101)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(topicDtoClosed).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(beforeCount).isEqualTo(topicDao.getAll().size());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void testOpenTopicNotFound() throws Exception {
        int beforeCount = topicDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{topicId}/open", 108562)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(topicDtoClosed).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(topicDao.getAll().size());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void testUpdateTopicSuccess() throws Exception {

        topicDtoUpdate = new TopicDto(101L, "Почему Земля круглая?", "Какой то контент",
                LocalDateTime.of(2021, 2, 7, 22, 00, 00),
                LocalDateTime.of(2021, 2, 7, 22, 00, 00),
                userInfoDto, commentDtoList);

        int beforeCount = topicDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(topicDtoUpdate).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(beforeCount).isEqualTo(topicDao.getAll().size());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void testUpdateTopicNotFound() throws Exception {
        int beforeCount = topicDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(topicDtoNotFound).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(topicDao.getAll().size());
    }
}