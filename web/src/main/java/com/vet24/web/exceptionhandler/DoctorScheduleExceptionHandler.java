package com.vet24.web.exceptionhandler;

import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.exception.UnprocessableEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class DoctorScheduleExceptionHandler {

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ExceptionDto> handleException(UnprocessableEntityException exception, HttpServletRequest request) {
        ExceptionDto data = new ExceptionDto(exception.getMessage());
        log.info("Nested Servlet Exception, " + request.getRequestURI());
        return new ResponseEntity<>(data, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
