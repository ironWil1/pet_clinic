package com.vet24.security.exceptions.petContact;

public class NoSuchPetForPetContactException extends RuntimeException {

    public NoSuchPetForPetContactException(String message) {
        super(message);
    }
}
