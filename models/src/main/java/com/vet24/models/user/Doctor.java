package com.vet24.models.user;

import com.vet24.models.medicine.Appointment;
import com.vet24.models.medicine.Diagnosis;
import com.vet24.models.pet.clinicalexamination.ClinicalExamination;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;


import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("DOCTOR")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Doctor extends User {

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

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "doctor")
    private List<DoctorReview> doctorReviews = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor")
    private List<ClinicalExamination> clinicalExaminations = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor")
    private List<Appointment> appointments = new ArrayList<>();

    public Doctor() {
        super();
    }

    public Doctor(String email, String password, Role role) {

        super(email, password, role);
    }
}
