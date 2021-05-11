package com.vet24.models.pet;

import com.vet24.models.user.Role;
import lombok.*;

import javax.persistence.*;
import java.util.Set;


@Entity
@Data
//@NoArgsConstructor
@AllArgsConstructor
//@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("CAT")
public class Cat extends Pet{

    @Id
    //@GeneratedValue(strategy = GenerationType.TABLE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String petName;

    @NonNull
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private PetContact petContact;

    public Cat() {
        super();
    }

    public Cat(String petName, PetContact petContact) {
        super(petName);
        this.petContact = petContact;
    }
}
