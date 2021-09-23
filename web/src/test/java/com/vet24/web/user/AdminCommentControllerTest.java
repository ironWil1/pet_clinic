package com.vet24.web.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.dto.user.CommentDto;
import com.vet24.web.ControllerAbstractIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WithUserDetails(value = "user3@gmail.com")
@Slf4j
public class AdminCommentControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "http://localhost:8080/api/admin/comment";
    private CommentDto commentDto;
//    private CommentService commentService;
//
//    @Autowired
//    public AdminCommentControllerTest(CommentService commentService) {
//        this.commentService = commentService;
//    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/admins.yml", "/datasets/comments.yml", "/datasets/clients.yml"})
    public void commentUpdatedNotFound() throws Exception {
        commentDto = new CommentDto();
        commentDto.setContent("testComment");
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 1_000_000)
                        .content(objectMapper.valueToTree(commentDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/admins.yml", "/datasets/comments.yml", "/datasets/clients.yml"})
    public void commentUpdated() throws Exception {
        commentDto = new CommentDto();
        commentDto.setContent("testComment");
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{id}", 101)
                        .content(objectMapper.valueToTree(commentDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/admins.yml", "/datasets/comments.yml", "/datasets/clients.yml"})
    public void commentDeleted() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{id}", 101))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
