package com.vet24.security.exceptions.petContact;

public class NoSuchPetContactIdException extends RuntimeException {

    public NoSuchPetContactIdException(String message) {
        super(message);
    }
}
