package com.vet24.web.exceptionhandler;

import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.exception.UnprocessableEntityDoctorScheduleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class DoctorScheduleExceptionHandler {

    @ExceptionHandler(UnprocessableEntityDoctorScheduleException.class)
    public ResponseEntity<ExceptionDto> handleException(UnprocessableEntityDoctorScheduleException exception) {
        ExceptionDto data = new ExceptionDto(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
