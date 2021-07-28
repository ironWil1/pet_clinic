package com.vet24.web.exceptionhandler;

import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.webjars.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class RequestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDto> handleException(NotFoundException exception, HttpServletRequest request) {
        ExceptionDto data = new ExceptionDto(exception.getMessage());
        log.info("Not found exception, " + request.getRequestURI());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionDto> handleException(BadRequestException exception, HttpServletRequest request) {
        ExceptionDto data = new ExceptionDto(exception.getMessage());
        log.info("Bad request exception, " + request.getRequestURI());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> handleException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        String exceptionMessage = exception.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage).collect(Collectors.joining(";"));
        ExceptionDto data = new ExceptionDto(exceptionMessage);
        log.info("Method argument not valid exception, " + request.getRequestURI());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDto> handleException(ConstraintViolationException exception, HttpServletRequest request) {
        ExceptionDto data = new ExceptionDto(exception.getMessage());
        log.info("Constraint violation exception, " + request.getRequestURI());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

}
