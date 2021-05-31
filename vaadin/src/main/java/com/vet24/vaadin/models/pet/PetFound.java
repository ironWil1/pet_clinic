package com.vet24.vaadin.models.pet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetFound {

    private Long id;
    private String latitude;
    private String longitude;
    private String text;
}

