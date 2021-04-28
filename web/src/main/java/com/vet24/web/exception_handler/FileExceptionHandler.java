package com.vet24.web.exception_handler;

import com.vet24.models.exception.StorageException;
import com.vet24.web.dto.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.FileNotFoundException;

@ControllerAdvice
public class FileExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handleException(FileNotFoundException exception) {
        ExceptionDto data = new ExceptionDto();
        data.setMessage(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handleException(StorageException exception) {
        ExceptionDto data = new ExceptionDto();
        data.setMessage(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handleException(Exception exception) {
        ExceptionDto data = new ExceptionDto();
        data.setMessage(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
