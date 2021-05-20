package com.vet24.models.exception;


import org.springframework.validation.Errors;

public class RegistrationDataInconsistentException extends RuntimeException{

    private Errors errors;

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }

    public RegistrationDataInconsistentException(Errors errors) {
        super();
        this.errors = errors;
    }

    public RegistrationDataInconsistentException(String message) {
        super(message);
    }
    public RegistrationDataInconsistentException(String message, Throwable cause) {
        super(message, cause);
    }
}
