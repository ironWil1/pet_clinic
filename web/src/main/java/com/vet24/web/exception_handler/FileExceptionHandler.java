package com.vet24.web.exception_handler;

import com.vet24.web.dto.FileExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.FileNotFoundException;

@ControllerAdvice
public class FileExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<FileExceptionDto> handleException(FileNotFoundException exception) {
        FileExceptionDto data = new FileExceptionDto();
        data.setMessage(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<FileExceptionDto> handleException(Exception exception) {
        FileExceptionDto data = new FileExceptionDto();
        data.setMessage(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
