package com.vet24.web.exceptionhandler;

import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.exception.RegistrationDataInconsistentException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.mail.MessagingException;
import java.util.stream.Collectors;

@ControllerAdvice
public class RegistrationExceptionHandler {

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionDto> handleException(MessagingException exception) {
        ExceptionDto data = new ExceptionDto();
        data.setMessage(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(RegistrationDataInconsistentException.class)
    public ResponseEntity<ExceptionDto> handleException(RegistrationDataInconsistentException exception) {
        ExceptionDto data = new ExceptionDto();
        StringBuilder mesBuild= new StringBuilder();
        if(exception.getErrors()!=null && exception.getErrors().hasErrors()){
            mesBuild.append(exception.getErrors().getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage).collect(Collectors.joining( ";" )));
        }
        mesBuild.append(exception.getMessage());
        data.setMessage(mesBuild.toString());
        return new ResponseEntity<>(data, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDto> handleException(DataIntegrityViolationException exception) {
        ExceptionDto data = new ExceptionDto();
        data.setMessage(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
