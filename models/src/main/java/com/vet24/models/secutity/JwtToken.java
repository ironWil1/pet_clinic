package com.vet24.models.secutity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class JwtToken {

    @Id
    private String token;

    public JwtToken() {
        super();
    }

    public JwtToken(String token){
        this.token = token;
    }



}
