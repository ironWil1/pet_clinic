package com.vet24.models.user;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Data
@DiscriminatorValue("ADMIN")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Admin extends User {
    public Admin() {
        super();
    }

    public Admin(@NonNull String email,
                 @NonNull String password,
                 @NonNull Role role) {
        super(email, password, role);
    }
}
