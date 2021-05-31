package com.vet24.vaadin.models.pet;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class PetContact {

    private Long id;

    @NonNull
    private String ownerName;

    @NonNull
    private String address;

    @NonNull
    private Long phone;

    @NonNull
    private String petCode;

    private Pet pet;
}