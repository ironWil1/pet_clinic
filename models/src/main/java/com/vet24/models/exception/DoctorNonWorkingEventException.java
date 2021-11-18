package com.vet24.models.exception;

public class DoctorNonWorkingEventException extends IllegalArgumentException{
    public DoctorNonWorkingEventException(String message) {
        super(message);
    }
}
