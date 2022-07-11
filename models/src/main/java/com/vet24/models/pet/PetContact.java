package com.vet24.models.pet;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "pet_contact")
public class PetContact {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "owner_name")
    private String ownerName;

    @NonNull
    @Column(name = "address")
    private String address;

    @NonNull
    @Column(name = "phone")
    private Long phone;

    @NonNull
    @Column(name = "description")
    private String description;

    @NonNull
    @Column(unique = true, updatable = false, name = "code")
    private String code;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private Pet pet;
}