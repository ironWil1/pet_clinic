package com.vet24.models.user;

import com.vet24.models.medicine.Diagnosis;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("DOCTOR")
@EqualsAndHashCode(callSuper = true,exclude = "comments,diagnoses")
@AllArgsConstructor
public class Doctor extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(
            mappedBy = "doctor",
            cascade = CascadeType.ALL
    )
    private Set<Diagnosis> diagnoses = new HashSet<>();

    @OneToMany(
            mappedBy = "doctor",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private List<Comment> comments;

    public Doctor() {
        super();
    }

    public Doctor(String firstname, String lastname, String email, String password, Role role) {
        super(firstname, lastname, email, password, role);
    }
}
