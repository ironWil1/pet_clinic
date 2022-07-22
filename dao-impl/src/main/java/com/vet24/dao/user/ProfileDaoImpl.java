package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.Profile;
import org.springframework.stereotype.Repository;

@Repository
public class ProfileDaoImpl extends ReadWriteDaoImpl<Long, Profile> implements ProfileDao  {
}
