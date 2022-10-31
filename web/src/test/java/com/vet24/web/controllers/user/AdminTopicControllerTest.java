package com.vet24.web.controllers.user;

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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminTopicControllerTest extends ControllerAbstractIntegrationTest {
    private final String URI = "/api/admin/topic";
    private String token;
    private static TopicDto topicDtoClosed;
    private static TopicDto topicDtoOpen;
    private static TopicDto topicDtoUpdate;
    private static TopicDto topicDtoNotFound;
    private static final List<CommentDto> commentDtoList = new ArrayList<>();


    @BeforeClass
    public static void createTopicDto() {
        UserInfoDto userInfoDto = new UserInfoDto(3L, "user3@gmail.com", "Ivan", "Ivanov");
        CommentDto commentDto = new CommentDto();
        commentDto.setId(101L);
        commentDto.setContent("right  comment");
        commentDto.setDateTime(LocalDateTime.of(2021, 6, 8, 14, 20, 00));
        commentDto.setUserInfoDto(userInfoDto);
        commentDto.setLikes(0);
        commentDto.setDislike(0);
        commentDtoList.add(commentDto);
        topicDtoOpen = new TopicDto(101L, "Почему Земля круглая?", "Какой-то контент"
                , LocalDateTime.of(2021, 2, 7, 22, 00, 00)
                , LocalDateTime.of(2021, 2, 7, 22, 00, 00)
                , userInfoDto, commentDtoList);
        topicDtoClosed = new TopicDto(100L, "Какой сегодня день?", "Что-то понаписал"
                , LocalDateTime.of(2021, 1, 2, 00, 00, 00)
                , LocalDateTime.of(2021, 6, 8, 14, 20, 00)
                , userInfoDto, commentDtoList);
        topicDtoNotFound = new TopicDto(100584L, "Тест", "Тест"
                , LocalDateTime.of(2021, 1, 2, 00, 00, 00)
                , LocalDateTime.of(2021, 6, 8, 14, 20, 00)
                , userInfoDto, commentDtoList);
        topicDtoUpdate = new TopicDto(101L, "Почему Земля круглая?", "Какой то контент",
                LocalDateTime.of(2021, 2, 7, 22, 00, 00),
                LocalDateTime.of(2021, 2, 7, 22, 00, 00),
                userInfoDto, commentDtoList);
    }

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("admin1@email.com", "admin");
    }

    private int getCount() {
        TypedQuery<Topic> query = entityManager.createQuery("SELECT t FROM Topic t", Topic.class);
        return query.getResultList().size();
    }

    private ResultMatcher jsonArrayValidate(Integer arrayIndex, TopicDto topicDto) {
        return ResultMatcher.matchAll(
                jsonPath("$[" + arrayIndex + "].title", is(topicDto.getTitle())),
                jsonPath("$[" + arrayIndex + "].content", is(topicDto.getContent())),
                jsonPath("$[" + arrayIndex + "].creationDate", containsString(topicDto.getCreationDate().toString())),
                jsonPath("$[" + arrayIndex + "].lastUpdateDate", containsString(topicDto.getLastUpdateDate().toString())),
                jsonPath("$[" + arrayIndex + "].topicStarter.id", is(topicDto.getTopicStarter().getId().intValue())),
                jsonPath("$[" + arrayIndex + "].topicStarter.email", is(topicDto.getTopicStarter().getEmail())),
                jsonPath("$[" + arrayIndex + "].topicStarter.firstname", is(topicDto.getTopicStarter().getFirstname())),
                jsonPath("$[" + arrayIndex + "].topicStarter.lastname", is(topicDto.getTopicStarter().getLastname())),
                jsonPath("$[" + arrayIndex + "].commentDtoList", is(notNullValue())));
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/adminTopicControllerTest/user-entities.yml",
            "/datasets/controllers/adminTopicControllerTest/topics.yml",
            "/datasets/controllers/adminTopicControllerTest/comments.yml"})
    public void testDeleteTopicSuccess() throws Exception {
        int result = getCount();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{topicId}", 100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(topicDtoClosed))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        boolean isEntityDelete = entityManager.createQuery("SELECT CASE WHEN COUNT(t) = 0 THEN TRUE ELSE FALSE END" +
                        " FROM Topic t WHERE t.id =: id AND t.title =: title AND t.content =: content", Boolean.class)
                .setParameter("id", 100L)
                .setParameter("title", topicDtoClosed.getTitle())
                .setParameter("content", topicDtoClosed.getContent()).getSingleResult();
        assertTrue("Тест не пройден! Сущность с указанным айди не удалилась!", isEntityDelete);
        assertThat(--result).isEqualTo(getCount());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/adminTopicControllerTest/user-entities.yml",
            "/datasets/controllers/adminTopicControllerTest/topics.yml",
            "/datasets/controllers/adminTopicControllerTest/comments.yml"})
    public void testDeleteTopicNotFound() throws Exception {
        int result = getCount();
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{topicId}", 33)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(topicDtoClosed))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        assertThat(result).isEqualTo(getCount());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/adminTopicControllerTest/user-entities.yml",
            "/datasets/controllers/adminTopicControllerTest/topics.yml",
            "/datasets/controllers/adminTopicControllerTest/comments.yml"})
    public void testCloseTopicSuccess() throws Exception {
        int result = getCount();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{topicId}/close", 100)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(topicDtoClosed))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        boolean isEntityClosed = entityManager.createQuery("SELECT CASE WHEN COUNT(t) = 1 THEN TRUE ELSE FALSE END " +
                        "FROM Topic t WHERE t.id =: id AND t.isClosed =: isClosed", Boolean.class)
                .setParameter("id", 100L).setParameter("isClosed", true).getSingleResult();
        assertTrue("Тест не пройден! Топик с указанным айди не закрылся!", isEntityClosed);
        assertThat(result).isEqualTo(getCount());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/adminTopicControllerTest/user-entities.yml",
            "/datasets/controllers/adminTopicControllerTest/topics.yml",
            "/datasets/controllers/adminTopicControllerTest/comments.yml"})
    public void testCloseTopicNotFound() throws Exception {
        int result = getCount();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{topicId}/close", 108562)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(topicDtoClosed))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        assertThat(result).isEqualTo(getCount());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/adminTopicControllerTest/user-entities.yml",
            "/datasets/controllers/adminTopicControllerTest/topics.yml",
            "/datasets/controllers/adminTopicControllerTest/comments.yml"})
    public void testOpenTopicSuccess() throws Exception {
        int result = getCount();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{topicId}/open", 101)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(topicDtoOpen))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        boolean isEntityOpen = entityManager.createQuery("SELECT CASE WHEN COUNT(t) = 1 THEN TRUE ELSE FALSE END " +
                        "FROM Topic t WHERE t.id =: id AND t.isClosed =: isClosed", Boolean.class)
                .setParameter("id", 101L).setParameter("isClosed", false).getSingleResult();
        assertTrue("Тест не пройден! Топик с указанным айди не открылся!", isEntityOpen);
        assertThat(result).isEqualTo(getCount());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/adminTopicControllerTest/user-entities.yml",
            "/datasets/controllers/adminTopicControllerTest/topics.yml",
            "/datasets/controllers/adminTopicControllerTest/comments.yml"})
    public void testOpenTopicNotFound() throws Exception {
        int result = getCount();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{topicId}/open", 108562)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(topicDtoOpen).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        assertThat(result).isEqualTo(getCount());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/adminTopicControllerTest/user-entities.yml",
            "/datasets/controllers/adminTopicControllerTest/topics.yml",
            "/datasets/controllers/adminTopicControllerTest/comments.yml"})
    public void testUpdateTopicSuccess() throws Exception {
        int result = getCount();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{topicId}", 101)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(topicDtoUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isOk(),
                        jsonPath("$.id").value(101L),
                        jsonPath("$.title").value(topicDtoUpdate.getTitle()),
                        jsonPath("$.content").value(topicDtoUpdate.getContent()),
                        jsonPath("$.creationDate", containsString(topicDtoUpdate.getCreationDate().toString())),
                        jsonPath("$.lastUpdateDate").value(notNullValue()),
                        jsonPath("$.topicStarter.id").value(topicDtoUpdate.getTopicStarter().getId().intValue()),
                        jsonPath("$.topicStarter.email").value(topicDtoUpdate.getTopicStarter().getEmail()),
                        jsonPath("$.topicStarter.firstname").value(topicDtoUpdate.getTopicStarter().getFirstname()),
                        jsonPath("$.topicStarter.lastname").value(topicDtoUpdate.getTopicStarter().getLastname()),
                        jsonPath("$.commentDtoList").value(notNullValue()));
        boolean isEntityUpdate = entityManager.createQuery("SELECT CASE WHEN COUNT(t) = 1 THEN TRUE ELSE FALSE END " +
                        "FROM Topic t WHERE t.id =: id AND t.title =: title AND t.content =: content", Boolean.class)
                .setParameter("id", 101L).setParameter("title", topicDtoUpdate.getTitle())
                .setParameter("content", topicDtoUpdate.getContent()).getSingleResult();
        assertTrue("Тест не пройден! Топик с указанным айди не обновился", isEntityUpdate);
        assertThat(result).isEqualTo(getCount());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/adminTopicControllerTest/user-entities.yml",
            "/datasets/controllers/adminTopicControllerTest/topics.yml",
            "/datasets/controllers/adminTopicControllerTest/comments.yml"})
    public void testUpdateTopicNotFound() throws Exception {
        int result = getCount();
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{topicId}", 100584)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(topicDtoNotFound))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        boolean isEntityNotUpdate = entityManager.createQuery("SELECT CASE WHEN COUNT(t) = 0 THEN TRUE ELSE FALSE END " +
                        "FROM Topic t WHERE t.title =: title AND t.content =: content", Boolean.class)
                .setParameter("title", topicDtoNotFound.getTitle())
                .setParameter("content", topicDtoNotFound.getContent()).getSingleResult();
        assertTrue("Тест не пройден! Топик обновился", isEntityNotUpdate);
        assertThat(result).isEqualTo(getCount());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/adminTopicControllerTest/user-entities.yml",
            "/datasets/controllers/adminTopicControllerTest/topics.yml",
            "/datasets/controllers/adminTopicControllerTest/comments.yml"})
    public void getAllTopics() throws Exception {
        int result = getCount();
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/allTopics")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100L))
                .andExpect(jsonPath("$[1].id").value(101L))
                .andExpect(jsonArrayValidate(0, topicDtoClosed))
                .andExpect(jsonArrayValidate(1, topicDtoOpen))
                .andExpect(jsonPath("$").value(hasSize(2)));
        assertThat(result).isEqualTo(getCount());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/controllers/adminTopicControllerTest/user-entities.yml",
            "/datasets/controllers/adminTopicControllerTest/topics.yml",
            "/datasets/controllers/adminTopicControllerTest/comments.yml"})
    public void getTopicById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/{topicId}", 101)
                        .header("Authorization", "Bearer " + token))
                .andExpect(jsonPath("$.id").value(101L))
                .andExpect(status().isOk());
    }
}