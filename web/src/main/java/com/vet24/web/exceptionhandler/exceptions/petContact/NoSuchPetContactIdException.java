package com.vet24.web.exceptionhandler.exceptions.petContact;

public class NoSuchPetContactIdException extends RuntimeException {

    public NoSuchPetContactIdException(String message) {
        super(message);
    }
}
