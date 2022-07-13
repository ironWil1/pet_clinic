package com.vet24.service.user;

import com.vet24.dao.ReadWriteDao;
import com.vet24.dao.user.ProfileDao;
import com.vet24.models.user.Profile;
import com.vet24.models.user.User;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl extends ReadWriteServiceImpl<Long, Profile> implements ProfileService{

    private final ProfileDao profileDao;

    protected ProfileServiceImpl(ProfileDao profileDao) {
        super(profileDao);
        this.profileDao = profileDao;
    }
}
