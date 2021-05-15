package com.vet24.security.handlers.petContact;

import com.vet24.security.exceptions.petContact.NoSuchPetContactIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PetContactGlobalExceptionHandler {

//     Обработка ошибок:
//     ResponseEntity - обёртка HTTP response
//     <UserIncorrectData> - Тип объекта, который добавляется в HTTP response body
//     NoSuchUserException - exception, на который должен реагировать данный метод

    @ExceptionHandler
    public ResponseEntity<PetContactIncorrectData> handleException(NoSuchPetContactIdException exception) {
        PetContactIncorrectData data = new PetContactIncorrectData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<PetContactIncorrectData> handleException(NumberFormatException exception) {
        PetContactIncorrectData data = new PetContactIncorrectData();
        data.setInfo("Проверьте, что вы передали верное значение в URL: " + exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
