package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.user.TopicDao;
import com.vet24.models.dto.user.CommentDto;
import com.vet24.models.dto.user.TopicDto;
import com.vet24.models.dto.user.UserInfoDto;
import com.vet24.models.mappers.user.TopicMapper;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AdminTopicControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    AdminTopicController adminTopicController;
    @Autowired
    TopicDao topicDao;
    @Autowired
    TopicMapper topicMapper;

    final String URI = "http://localhost:8090/api/admin/topic";
    final HttpHeaders HEADERS = new HttpHeaders();

    TopicDto topicDtoClosed;
    TopicDto topicDtoOpen;
    UserInfoDto userInfoDto;
    CommentDto commentDto;
    List<CommentDto> commentDtoList;

    @Before
    public void createNewTopicDto() {
        this.userInfoDto = new UserInfoDto(3L, "user3@gmail.com", "Ivan", "Ivanov");
        this.commentDto = new CommentDto();
        this.commentDto.setId(101L);
        this.commentDto.setContent("right  comment");
        this.commentDto.setDateTime(LocalDateTime.of(2021, 6, 8, 14, 20, 00));
        this.commentDto.setUserInfoDto(userInfoDto);
        this.commentDto.setLikes(0);
        this.commentDto.setDislike(0);
        this.commentDtoList.add(commentDto);
        this.topicDtoClosed = new TopicDto(101L, "Почему Земля круглая?", "Какой то контент"
                , LocalDateTime.of(2021, 2, 7, 22, 00, 00)
                ,LocalDateTime.of(2021, 2, 7, 22, 00, 00)
        ,userInfoDto, commentDtoList);
        this.topicDtoClosed = new TopicDto(100L, "Какой сегодня день?", "Что то понаписал"
                ,LocalDateTime.of(2021, 1, 2, 00, 00, 00)
                ,LocalDateTime.of(2021, 6, 8, 14, 20, 00)
                ,userInfoDto, commentDtoList);

    }

    // +mock, delete topic by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void testDeleteTopicSuccess() throws Exception{
        int beforeCount = topicDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{topicId}", 100)
                        .content(objectMapper.valueToTree(topicDtoClosed).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(--beforeCount).isEqualTo(topicDao.getAll().size());
    }

    // +mock, delete topic by id - topic not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void testDeleteTopicErrorNotFound() throws Exception {
        int beforeCount = topicDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{topicId}", 33)
                        .content(objectMapper.valueToTree(topicDtoClosed).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(topicDao.getAll().size());
    }

    // +mock, close topic - Success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void testCloseTopicSuccess() throws Exception {
        int beforeCount = topicDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{topicId}/close", 100)
                        .content(objectMapper.valueToTree(topicDtoOpen).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(beforeCount).isEqualTo(topicDao.getAll().size());
    }

// +mock, close topic by id - NotFound
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void testCloseTopicByIdNotFound() throws Exception {
        int beforeCount = topicDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{topicId}/close", 108562)
                        .content(objectMapper.valueToTree(topicDtoClosed).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(topicDao.getAll().size());
    }

    // +mock, open topic  - Success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void testOpenTopicSuccess() throws Exception {
        int beforeCount = topicDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{topicId}/open", 101)
                        .content(objectMapper.valueToTree(topicDtoClosed).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(beforeCount).isEqualTo(topicDao.getAll().size());
    }

// +mock, open topic where it closed find by id - NotFound

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void testOpenTopicByIdNotFound() throws Exception {
        int beforeCount = topicDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{topicId}/open", 108562)
                        .content(objectMapper.valueToTree(topicDtoClosed).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(topicDao.getAll().size());
    }

    // +mock, put topic by id - Success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void testUpdateTopicSuccess() throws Exception {
        int beforeCount = topicDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{topicId}", 100)
                        .content(objectMapper.valueToTree(topicDtoOpen).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
               ;
        assertThat(beforeCount).isEqualTo(topicDao.getAll().size());
    }
    // +mock, put topic by id - NotFound
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml", "/datasets/comments.yml"})
    public void testUpdateTopicNotFound() throws Exception {
        int beforeCount = topicDao.getAll().size();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{topicId}", 108562)
                        .content(objectMapper.valueToTree(topicDtoClosed).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(beforeCount).isEqualTo(topicDao.getAll().size());
    }
}