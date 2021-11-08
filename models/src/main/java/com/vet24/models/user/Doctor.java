package com.vet24.models.user;

import com.vet24.models.medicine.Diagnosis;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("DOCTOR")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Doctor extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @OneToMany(
            mappedBy = "doctor",
            cascade = CascadeType.ALL
    )
    private List<Diagnosis> diagnoses = new ArrayList<>();

    @OneToMany(
            mappedBy = "doctor",
            cascade = CascadeType.ALL
    )
    private List<DoctorNonWorking> doctorNonWorkings = new ArrayList<>();


    public Doctor() {
        super();
    }

    public Doctor(String firstname, String lastname, String email, String password, Role role) {
        super(firstname, lastname, email, password, role);
    }
}
