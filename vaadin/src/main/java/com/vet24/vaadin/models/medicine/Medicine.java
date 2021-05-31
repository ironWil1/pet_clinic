package com.vet24.vaadin.models.medicine;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Medicine {

    private Long id;

    private String manufactureName;

    private String name;

    private String icon;

    private String description;

    public Medicine(String manufactureName, String name, String icon, String description) {
        this.manufactureName = manufactureName;
        this.name = name;
        this.icon = icon;
        this.description = description;
    }
}
