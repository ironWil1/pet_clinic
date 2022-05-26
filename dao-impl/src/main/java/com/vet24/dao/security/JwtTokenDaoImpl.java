package com.vet24.dao.security;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.secutity.JwtToken;
import org.springframework.stereotype.Repository;

@Repository
public class JwtTokenDaoImpl extends ReadWriteDaoImpl<String, JwtToken> implements JwtTokenDao {

}
