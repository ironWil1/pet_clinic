package com.vet24.service.security;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.secutity.JwtToken;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl extends ReadWriteServiceImpl<String, JwtToken> implements JwtTokenService {
    protected JwtServiceImpl(ReadWriteDao<String, JwtToken> readWriteDao) {
        super(readWriteDao);
    }
}
