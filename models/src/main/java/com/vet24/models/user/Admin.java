package com.vet24.models.user;

import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
@EqualsAndHashCode(callSuper = true)
public class Admin extends User {

    /*public Admin() {
        super();
    }

    public Admin(String firstname, String lastname, String email, String password, Role role) {
        super(firstname, lastname, email, password, role);
    }*/
}
