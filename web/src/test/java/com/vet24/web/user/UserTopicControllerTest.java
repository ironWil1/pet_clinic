package com.vet24.web.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.user.TopicDaoImpl;
import com.vet24.models.dto.user.CommentDto;
import com.vet24.models.dto.user.TopicDto;
import com.vet24.models.dto.user.UserInfoDto;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class UserTopicControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    TopicDaoImpl topicDao;

    private final String URL = "/api/user/topic";
    private String token;
    static TopicDto topicDto;
    static UserInfoDto userInfoDto;
    static CommentDto commentDto;
    static List<CommentDto> commentDtoList = new ArrayList<>();


    @BeforeClass
    public static void createTopicDto() {
        userInfoDto = new UserInfoDto(3L, "user3@gmail.com", "Ivan", "Ivanov");
        commentDto = new CommentDto();
        commentDto.setId(101L);
        commentDto.setContent("some comment");
        commentDto.setDateTime(LocalDateTime.of(2022, 1, 1, 10, 0, 0));
        commentDto.setUserInfoDto(userInfoDto);
        commentDto.setLikes(1);
        commentDto.setDislike(0);
        commentDtoList.add(commentDto);

        topicDto = new TopicDto(101L, "Title", "Content",
                LocalDateTime.of(2022, 1, 2, 20, 0, 0),
                LocalDateTime.of(2022, 1, 2, 21, 1, 1),
                userInfoDto, commentDtoList);
    }

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("user3@gmail.com", "user3");
    }

    private int getCount(){
        int count = topicDao.getAll().size();
        return count;
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml"})
    public void getAllTopics() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/allTopics", List.class)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml"})
    public void getYourTopics() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/yourTopics", List.class)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml"})
    public void getTopicById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{topicId}", 101)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml"})
    public void createTopic() throws Exception {
        int result = getCount();
        mockMvc.perform(MockMvcRequestBuilders.post(URL, 105)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.valueToTree(topicDto).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        assertThat(++result).isEqualTo(getCount());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml"})
    public void updateTopic() throws Exception {
        int result = getCount();
        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/{topicId}", 101)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(new TopicDto(101L,"Title update", "Content update",
                                LocalDateTime.of(2022, 1, 2, 22, 0, 0),
                                LocalDateTime.of(2022, 1, 2, 23, 1, 1),
                                userInfoDto, commentDtoList)).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(result).isEqualTo(getCount());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml"})
    public void deleteTopic() throws Exception {
        int result = getCount();
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{topicId}", 101)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(topicDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(--result).isEqualTo(getCount());
    }
}
