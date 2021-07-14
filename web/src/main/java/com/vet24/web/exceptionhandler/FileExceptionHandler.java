package com.vet24.web.exceptionhandler;

import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.exception.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;

@ControllerAdvice
@Slf4j
public class FileExceptionHandler {

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleException(FileNotFoundException exception, HttpServletRequest request) {
        ExceptionDto data = new ExceptionDto();
        data.setMessage(exception.getMessage());

        log.info("File not found, " + request.getRequestURI());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ExceptionDto> handleException(StorageException exception, HttpServletRequest request) {
        ExceptionDto data = new ExceptionDto();
        data.setMessage(exception.getMessage());
        log.info("Storage not found " + request.getRequestURI());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }
}
