package com.vet24.models.user;

import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("MANAGER")
@EqualsAndHashCode(callSuper = true)
public class Manager extends User {

    public Manager(String email, String password, Role role) {
        super(email, password, role);
    }
    public Manager() {
    }
}
