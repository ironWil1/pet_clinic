package com.vet24.models.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@DiscriminatorValue("DOCTOR")
@EqualsAndHashCode(callSuper = true, exclude = {"diagnoses"} )
@AllArgsConstructor
public class Doctor extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    public Doctor() {
        super();
    }

    public Doctor(String firstname, String lastname, String email, String password, Role role) {
        super(firstname, lastname, email, password, role);
    }
}
