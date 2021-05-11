package com.vet24.models.pet;

import com.vet24.models.user.Role;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Cat extends Pet{

    /*@Id
    //@GeneratedValue(strategy = GenerationType.TABLE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;*/

    /*@NonNull
    private String petName;*/

    //@NonNull
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    /*@JoinTable(name = "cat_petcontact", joinColumns = @JoinColumn(name = "pet_id"),
            inverseJoinColumns = @JoinColumn(name = "petcontact_id"))*/
    private PetContact petContact;

    public Cat() {
        super();
    }

    public Cat(String petName, PetContact petContact) {
        super(petName);
        this.petContact = petContact;
    }
}
