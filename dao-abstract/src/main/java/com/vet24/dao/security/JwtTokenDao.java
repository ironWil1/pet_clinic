package com.vet24.dao.security;


import com.vet24.dao.ReadWriteDao;
import com.vet24.models.secutity.JwtToken;

public interface JwtTokenDao extends ReadWriteDao<String, JwtToken> {
}
