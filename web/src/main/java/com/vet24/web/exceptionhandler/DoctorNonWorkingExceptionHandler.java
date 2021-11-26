package com.vet24.web.exceptionhandler;

import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.exception.DoctorNonWorkingEventException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class DoctorNonWorkingExceptionHandler {
    @ExceptionHandler(DoctorNonWorkingEventException.class)
    public ResponseEntity<ExceptionDto> handleException(DoctorNonWorkingEventException exception, HttpServletRequest request) {
        log.info("DoctorNonWorking exception" + request.getRequestURI());
        ExceptionDto data = new ExceptionDto();
        data.setMessage(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
