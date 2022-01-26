package com.vet24.web.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.user.CommentDto;
import com.vet24.models.dto.user.TopicDto;
import com.vet24.models.dto.user.UserInfoDto;
import com.vet24.models.user.Topic;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class UserTopicControllerTest extends ControllerAbstractIntegrationTest {

    @PersistenceContext
    private EntityManager entityManager;


    private final String URL = "/api/user/topic";
    private String token;
    static TopicDto topicDto1;
    static TopicDto topicDto2;
    static TopicDto topicDtoCreateOrDelete;
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


        topicDto1 = new TopicDto(100L, "Какой сегодня день?", "Что то понаписал"
                , LocalDateTime.of(2021, 1, 2, 00, 00, 00)
                , LocalDateTime.of(2021, 6, 8, 14, 20, 00)
                , userInfoDto, commentDtoList);

        topicDto2 = new TopicDto(101L, "Почему Земля круглая?", "Какой то контент"
                , LocalDateTime.of(2021, 2, 7, 22, 00, 00)
                , LocalDateTime.of(2021, 2, 7, 22, 00, 00)
                , userInfoDto, commentDtoList);

        topicDtoCreateOrDelete = new TopicDto(102L, "Быть разработчиком здорово", "Тестирование",
                LocalDateTime.of(2022, 1, 2, 20, 0, 0),
                LocalDateTime.of(2022, 1, 2, 21, 1, 1),
                userInfoDto, commentDtoList);
    }

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("user3@gmail.com", "user3");
    }

    private int getCount(){
        TypedQuery<Topic> query = (TypedQuery<Topic>) entityManager.createNativeQuery("SELECT * FROM Topic", Topic.class);
        return query.getResultList().size();
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.[-1:].topicStarter.id").value(3))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml"})
    public void getTopicById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{topicId}", 101)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(101))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml"})
    public void getTopicByIdNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{topicId}", 154)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml"})
    public void createTopic() throws Exception {
        int result = getCount();
        mockMvc.perform(MockMvcRequestBuilders.post(URL, 102)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.valueToTree(topicDtoCreateOrDelete).toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        assertThat(++result).isEqualTo(getCount());
        entityManager.createQuery("SELECT t FROM Topic t where t.id = :id and t.content = :content and t.title = :title", Topic.class)
                .setParameter("id", 102L)
                .setParameter("content", "Тестирование")
                .setParameter("title", "Быть разработчиком здорово")
                .getResultList();
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml"})
    public void updateTopic() throws Exception {
        int result = getCount();
        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/{topicId}", 100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(new TopicDto(100L,"Нельзя сдаваться", "Выход всегда найдётся, главное не в окно",
                                LocalDateTime.of(2022, 1, 2, 22, 0, 0),
                                LocalDateTime.of(2022, 1, 2, 23, 1, 1),
                                userInfoDto, commentDtoList)).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(result).isEqualTo(getCount());
        entityManager.createQuery("SELECT t FROM Topic t where t.id = :id and t.content = :content and t.title = :title", Topic.class)
                .setParameter("id", 100L)
                .setParameter("content", "Выход всегда найдётся, но только не в окно")
                .setParameter("title", "Нельзя сдаваться")
                .getResultList();
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml"})
    public void updateTopicNotFound() throws Exception {
        int result = getCount();
        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/{topicId}", 5654451)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(new TopicDto(100L,"Нельзя сдаваться", "Выход всегда найдётся, главное не в окно",
                                LocalDateTime.of(2022, 1, 2, 22, 0, 0),
                                LocalDateTime.of(2022, 1, 2, 23, 1, 1),
                                userInfoDto, commentDtoList)).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        assertThat(result).isEqualTo(getCount());

    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml"})
    public void deleteTopic() throws Exception {
        int result = getCount();
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{topicId}", 101)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(topicDtoCreateOrDelete).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(--result).isEqualTo(getCount());
        entityManager.createQuery("SELECT t FROM Topic t where t.id = :id", Topic.class)
                .setParameter("id", 101L)
                .getResultList()
                .isEmpty();
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/topics.yml"})
    public void deleteTopicNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{topicId}", 46987845)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(topicDtoCreateOrDelete).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
