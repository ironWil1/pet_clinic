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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ManagerNewsControllerTest extends ControllerAbstractIntegrationTest {
    private final String URI = "/api/manager/news";

    private ManagerNewsRequestDto managerNewsSuccess;
    private ManagerNewsRequestDto managerNewsEmptyFirstField;
    private ManagerNewsRequestDto managerNewsEmptySecondField;
    private ManagerNewsRequestDto managerNewsEmptyThirdField;
    private ManagerNewsRequestDto managerNewsEmptyFifthField;

    private List<String> pictures;
    private List<String> pics;
    private int initialCount = 3;
    private String token;

    @Before
    public void createManagerNewsRequestDto() {
        managerNewsSuccess = new ManagerNewsRequestDto("news", "content", NewsType.PROMOTION, true, LocalDateTime.now());
        managerNewsEmptyFirstField = new ManagerNewsRequestDto(null , "content", NewsType.PROMOTION, true, LocalDateTime.now());
        managerNewsEmptySecondField = new ManagerNewsRequestDto("news", null, NewsType.PROMOTION, true, LocalDateTime.now());
        managerNewsEmptyThirdField = new ManagerNewsRequestDto("news", "content", null, true, LocalDateTime.now());
        managerNewsEmptyFifthField = new ManagerNewsRequestDto("news", "content", NewsType.PROMOTION, true, null);
        pics = new ArrayList<>();
        pics.add("https://wikipet.ru/wp-content/uploads/2022/10/8503d1ee-a17a-469d-bd83-0f2fe7def73a.jpeg");
    }

    @Before
    public void addPictures() {
        pictures = new ArrayList<>();
        pictures.add("https://wikipet.ru/uploads/posts/2018-09/1538037544_1.png");
        pictures.add("https://wikipet.ru/wp-content/uploads/2022/10/8503d1ee-a17a-469d-bd83-0f2fe7def73a.jpeg");
    }

    @Before
    public void setToken() throws Exception {
        token = getAccessToken("manager1@email.com", "manager");

    }

    private long getCountNews() {
        return entityManager.createQuery("SELECT COUNT(n) FROM News n", Long.class).getSingleResult();
    }

    private News getNews() {
        return entityManager.createQuery("select n from News n join fetch n.pictures where n.id = :id", News.class).setParameter("id", 202L).getSingleResult();
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void getAllNewsSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(303))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].title").value("news"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].content").value("content"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].type").value("UPDATING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].important").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].endTime").value("2022-07-27T20:09:00.712268"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].published").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].pictures")
                        .value("https://wikipet.ru/wp-content/uploads/2022/10/83ac817b-7b9a-4f38-a46a-4f36b9c679ae.jpeg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)));
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void getNewsByIdSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/" + 202)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(202))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("news"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("content"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("PROMOTION"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.important").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endTime").value("2022-09-27T20:09:00.712268"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.published").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pictures")
                        .value("https://wikipet.ru/wp-content/uploads/2022/10/8503d1ee-a17a-469d-bd83-0f2fe7def73a.jpeg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(8)));
    }

    // Новости с таким ID не существует для GET запроса
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void getNonExistingNewsError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI + "/" + 1000)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void persistNewsSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(managerNewsSuccess).toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        assertThat(++initialCount).isEqualTo(getCountNews());
    }

    // Были заполнены не все поля при создании Новости (1 поле)
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void persistNewsEmptyFirstFieldError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(managerNewsEmptyFirstField).toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    // Были заполнены не все поля при создании Новости (2 поле)
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void persistNewsEmptySecondFieldError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(managerNewsEmptySecondField).toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    // Были заполнены не все поля при создании Новости (3 поле)
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void persistNewsEmptyThirdFieldError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(managerNewsEmptyThirdField).toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    // Были заполнены не все поля при создании Новости (5 поле)
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void persistNewsEmptyFifthFieldError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(managerNewsEmptyFifthField).toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void updateNewsSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/" + 202)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(managerNewsSuccess).toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        assertThat(getNews().getTitle()).isEqualTo("news");
        assertThat(getNews().getContent()).isEqualTo("content");
        assertThat(getNews().getType()).isEqualTo(NewsType.PROMOTION);
        assertThat(getNews().getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).isEqualTo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        assertThat(new ArrayList<>(getNews().getPictures())).isEqualTo(pics);
        assertThat(getNews().isImportant()).isTrue();
        assertThat(getNews().isPublished()).isTrue();
    }

    // Новости с таким ID не существует для PUT запроса
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void updateNonExistingNewsSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/" + 1000)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(managerNewsSuccess).toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    // Были заполнены не все поля при изменении Новости (1 поле)
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void updateNonEmptyFirstFieldError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/" + 202)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(managerNewsEmptyFirstField).toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    // Были заполнены не все поля при изменении Новости (2 поле)
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void updateNonEmptySecondFieldError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/" + 202)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(managerNewsEmptySecondField).toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    // Были заполнены не все поля при изменении Новости (3 поле)
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void updateNonEmptyThirdFieldError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/" + 202)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(managerNewsEmptyThirdField).toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    // Были заполнены не все поля при изменении Новости (5 поле)
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void updateNonEmptyFifthFieldError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/" + 202)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(managerNewsEmptyFifthField).toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void deleteNewsSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 202)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        assertThat(--initialCount).isEqualTo(getCountNews());
    }

    // Новости с таким ID не существует для DELETE запроса
    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void deleteNonExistingNewsError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 1000)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void addNewsPictureSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}/pictures/", "202")
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.valueToTree(pictures).toString())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
        assertThat(new ArrayList<>(getNews().getPictures())).isEqualTo(pictures);
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void publishNewsSuccess() throws Exception {
        mockMvc. perform(MockMvcRequestBuilders.put(URI + "/publish")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(Arrays.asList(101, 202)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        News news1 = entityManager.find(News.class, 101L);
        News news2 = entityManager.find(News.class, 202L);
        assertThat(news1.isPublished()).isFalse(); //EndData новости уже прошла
        assertThat(news2.isPublished()).isTrue();
    }

    @Test
    @DataSet(cleanBefore = true, value = {
            "/datasets/controllers/user/managerNewsController/user_entities.yml",
            "/datasets/controllers/user/managerNewsController/news.yml",
            "/datasets/controllers/user/managerNewsController/news_pictures.yml"
    })
    public void unpublishNewsSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/unpublish")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.valueToTree(Arrays.asList(101, 202)).toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        News news1 = entityManager.find(News.class, 101L);
        News news2 = entityManager.find(News.class, 202L);
        assertThat(news1.isPublished()).isFalse();
        assertThat(news2.isPublished()).isTrue(); //endData новости уже прошла, нельзя снять с публикации
    }
}
