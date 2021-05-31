package com.vet24.models.user;

import com.vet24.models.medicine.Diagnosis;
<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
=======
import lombok.*;
>>>>>>> impl; exception handler simplified

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@DiscriminatorValue("DOCTOR")
@EqualsAndHashCode(callSuper = true,exclude = "diagnoses")
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

    public Doctor() {
        super();
    }

    public Doctor(String firstname, String lastname, String email, String password, Role role) {
        super(firstname, lastname, email, password, role);
    }
}
