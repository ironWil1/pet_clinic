package com.vet24.web.exceptionhandler.handlers.petContact;

import com.vet24.web.exceptionhandler.exceptions.petContact.NoSuchPetContactIdException;
import com.vet24.web.exceptionhandler.exceptions.petContact.NoSuchPetForPetContactException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PetContactGlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<PetContactExceptionDto> handleException(NoSuchPetContactIdException exception) {
        PetContactExceptionDto data = new PetContactExceptionDto();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<PetContactExceptionDto> handleException(NoSuchPetForPetContactException exception) {
        PetContactExceptionDto data = new PetContactExceptionDto();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<PetContactExceptionDto> handleException(NumberFormatException exception) {
        PetContactExceptionDto data = new PetContactExceptionDto();
        data.setInfo("Проверьте, что вы передали верное значение в URL: " + exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
