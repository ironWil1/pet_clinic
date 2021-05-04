package com.vet24.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PetType {
    CAT("CAT"),
    DOG("DOG");

    private final String type;

    PetType(String type) {
        this.type = type;
    }

    @JsonCreator
    public static PetType fromString(String type) {
        if (type != null) {
            for (PetType e : PetType.values()) {
                if (type.equalsIgnoreCase(e.type)) {
                    return e;
                }
            }
        }
        throw new IllegalArgumentException("No constant with type " + type + " has been found");
    }

    @JsonValue
    public String getType() {
        return type;
    }
}
