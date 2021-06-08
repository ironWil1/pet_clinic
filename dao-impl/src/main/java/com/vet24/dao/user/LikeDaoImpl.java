package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.Like;
import com.vet24.models.user.LikeId;
import org.springframework.stereotype.Repository;

@Repository
public class LikeDaoImpl extends ReadWriteDaoImpl<LikeId, Like> implements LikeDao{

}
