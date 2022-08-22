package com.vet24.models.pet.appearance;

import com.vet24.models.enums.PetType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "pet_breed", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"pet_type","breed"})})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Breed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(insertable = false, updatable = false, name = "pet_type")
    @Enumerated(EnumType.STRING)
    private PetType petType;

    @Column(name = "breed")
    private String breed;
}
