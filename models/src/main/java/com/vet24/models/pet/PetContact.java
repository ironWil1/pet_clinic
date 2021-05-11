package com.vet24.models.pet;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class PetContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String ownerName;

    @NonNull
    private String address;

    @NonNull
    @Column(unique = true)
    private Long phone;

    @NonNull
    @Column(unique = true)
    private String uniqCode; // уникальный код для животного, генерируется на сервере при создании этой сущности.

    @OneToOne(mappedBy = "petContact")
    private Pet pet;
}