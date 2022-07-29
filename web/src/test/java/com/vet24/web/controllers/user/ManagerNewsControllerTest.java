package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.user.ManagerNewsRequestDto;
import com.vet24.models.enums.NewsType;
import com.vet24.models.news.News;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ManagerNewsControllerTest extends ControllerAbstractIntegrationTest {
    private final String URI = "/api/manager/news";
    private ManagerNewsRequestDto requestDto;
    private List<String> pictures;
    private String token;

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("manager1@email.com", "manager");

        requestDto = new ManagerNewsRequestDto();
        requestDto.setContent("content");
        requestDto.setTitle("news");
        requestDto.setType(NewsType.PROMOTION);

        pictures = new ArrayList<>();
        pictures.add("picture1123");
        pictures.add("picture1125");
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void getAllNews_ShouldShowAllNews() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(303))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].title").value("news"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].content").value("content"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].type").value("UPDATING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].important").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].endTime").value("2022-07-27T20:09:00.712268"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].published").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].pictures").value("picture3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)));
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void getNewsById_withId_ShouldShowNewsById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/" + 202)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(202))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("news"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("content"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("PROMOTION"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.important").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endTime").value("2022-09-27T20:09:00.712268"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.published").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pictures").value("picture2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(8)));
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void persistNews_RequestDto_ShouldPersistNews() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());

        List<News> news = entityManager.createQuery("select n from News n where " +
                        "n.title = :title " +
                        "and n.type = :type " +
                        "and n.content = :content " +
                        "and n.id = :id")
                .setParameter("title", "news")
                .setParameter("type", NewsType.PROMOTION)
                .setParameter("content", "content")
                .setParameter("id", 1L)
                .getResultList();

        assertEquals(1, news.get(0).getId());
        assertEquals(NewsType.PROMOTION, news.get(0).getType());
        assertEquals("content", news.get(0).getContent());
        assertEquals("news", news.get(0).getTitle());
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void updateNewsById_RequestDto_ShouldUpdateNews() throws Exception {
        requestDto.setContent("content2");
        requestDto.setTitle("discord is on fire");
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/" + 202)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());

        List<News> news = entityManager.createQuery("select n from News n where n.id = :id")
                .setParameter("id", 202L)
                .getResultList();

        assertEquals("discord is on fire", news.get(0).getTitle());
        assertEquals("content2", news.get(0).getContent());
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void addNewsPicture_Pictures_ShouldAddNewsPicture() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}/pictures/", "202")
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(pictures))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        List<News> news = entityManager.createQuery("select n " +
                        "from News n " +
                        "join fetch n.pictures " +
                        "where n.id = :id")
                .setParameter("id", 202L)
                .getResultList();

        assertEquals("picture1123", news.get(0).getPictures().get(0));
        assertEquals("picture1125", news.get(0).getPictures().get(1));
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void publishNews_Ids_ShouldPublishNews() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/publish")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(Arrays.asList(101, 202)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());

        List<News> news = entityManager.createQuery("select n from News n where n.id in :ids")
                .setParameter("ids", Arrays.asList(101L, 202L))
                .getResultList();

        assertEquals(true, news.get(0).isPublished());
        assertEquals(true, news.get(1).isPublished());
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void unpublishNews_Ids_ShouldUnpublishNews() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/unpublish")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(Arrays.asList(101, 202)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());

        List<News> news = entityManager.createQuery("select n from News n where n.id in :ids")
                .setParameter("ids", Arrays.asList(101L, 202L))
                .getResultList();

        assertEquals(false, news.get(0).isPublished());
        assertEquals(false, news.get(1).isPublished());
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void deleteNewsById_Ids_ShoudDeleteNews() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", "101")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());

        List<News> news = entityManager.createQuery("select n from News n where n.id = : id")
                .setParameter("id", 101L)
                .getResultList();

        assertEquals(true, news.isEmpty());
    }
}
