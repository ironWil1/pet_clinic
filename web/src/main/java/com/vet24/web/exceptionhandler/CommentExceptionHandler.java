package com.vet24.web.exceptionhandler;

import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.exception.RepeatedCommentException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class CommentExceptionHandler {

    @ExceptionHandler(RepeatedCommentException.class)
    public ResponseEntity<ExceptionDto> handleException(RepeatedCommentException exception, HttpServletRequest request) {
        log.info("Тут случилось исключение, " + request.getRequestURI());
        ExceptionDto data = new ExceptionDto();
        data.setMessage(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDto> handleException(ConstraintViolationException exception, HttpServletRequest request) {
        ExceptionDto message = new ExceptionDto(exception.getMessage());
        log.info("Nested Servlet Exception, " + request.getRequestURI());
        return new ResponseEntity<>(message, HttpStatus.I_AM_A_TEAPOT);
    }
}
