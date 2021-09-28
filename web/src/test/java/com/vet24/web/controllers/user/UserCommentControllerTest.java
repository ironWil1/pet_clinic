package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.dao.user.CommentDao;
import com.vet24.service.user.ClientService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;


@WithUserDetails(value = "user1@gmail.com")
public class UserCommentControllerTest extends ClientControllerTest {

  /*  final String URI="api/user/comment";

    @Autowired
    private ClientService clientService;

    @Autowired
    private CommentDao commentDao;

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/comments.yml"})
    public void likeOrDislikeComment() {
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/comments.yml"})
    public void createOrUpdate() {
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/comments.yml"})
    public void removeComment() {
    }*/
}