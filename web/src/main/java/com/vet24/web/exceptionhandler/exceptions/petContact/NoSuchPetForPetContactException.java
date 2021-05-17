package com.vet24.web.exceptionhandler.exceptions.petContact;

public class NoSuchPetForPetContactException extends RuntimeException {

    public NoSuchPetForPetContactException(String message) {
        super(message);
    }
}
