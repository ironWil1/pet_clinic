package com.vet24.web.exceptionhandler;

import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.webjars.NotFoundException;

@ControllerAdvice
@Slf4j
public class RequestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDto> handleException(NotFoundException exception) {
        ExceptionDto data = new ExceptionDto(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionDto> handleException(BadRequestException exception) {
        ExceptionDto data = new ExceptionDto(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
