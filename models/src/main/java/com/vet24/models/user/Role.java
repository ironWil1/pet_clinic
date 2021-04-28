package com.vet24.models.user;

import com.vet24.models.enums.RoleNameEnum;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private RoleNameEnum name;


    public Role(){};

    public Role(RoleNameEnum name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleNameEnum getName() {
        return name;
    }

    public void setName(RoleNameEnum name) {
        this.name = name;
    }


    @Override
    public String getAuthority() {
        return name.toString();
    }
}
