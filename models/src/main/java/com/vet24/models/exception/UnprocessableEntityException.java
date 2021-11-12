package com.vet24.models.exception;

public class UnprocessableEntityException extends RuntimeException{

    public UnprocessableEntityException(String message) {
        super(message);
    }
}
