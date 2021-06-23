package com.vet24.models.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("DOCTOR")
@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Doctor extends User {

    @OneToMany(
            mappedBy = "doctor",
            cascade = CascadeType.ALL
    )
    private Set<Diagnosis> diagnoses = new HashSet<>();


    public Doctor() {
        super();
    }

    public Doctor(String firstname, String lastname, String email, String password, Role role) {
        super(firstname, lastname, email, password, role);
    }
}
