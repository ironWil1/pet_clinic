package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.Comment;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDaoImpl extends ReadWriteDaoImpl<Long, Comment>  implements CommentDao{

}
