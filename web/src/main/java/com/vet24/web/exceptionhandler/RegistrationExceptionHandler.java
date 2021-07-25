package com.vet24.web.exceptionhandler;

import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.exception.RepeatedRegistrationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class RegistrationExceptionHandler {


    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionDto> handleException(MessagingException exception, HttpServletRequest request) {
        ExceptionDto data = new ExceptionDto();
        data.setMessage(exception.getMessage());
        log.info("Messaging exception, " + request.getRequestURI());
        return new ResponseEntity<>(data, HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(RepeatedRegistrationException.class)
    public ResponseEntity<ExceptionDto> handleException(RepeatedRegistrationException exception, HttpServletRequest request) {
        ExceptionDto data = new ExceptionDto();
        data.setMessage(exception.getMessage());
        log.info("Registration exception, " + request.getRequestURI());
        return new ResponseEntity<>(data, HttpStatus.NOT_ACCEPTABLE);
    }

}
