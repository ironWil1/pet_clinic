package com.vet24.web.exceptionhandler;

import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.exception.RepeatedCommentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.logging.Logger;

@ControllerAdvice
public class CommentExceptionHandler {

    @Autowired
    private Logger logger;

    @ExceptionHandler(RepeatedCommentException.class)
    public ResponseEntity<ExceptionDto> handleException(RepeatedCommentException exception) {
        logger.info("Тут случилось исключение");
        ExceptionDto data = new ExceptionDto();
        data.setMessage(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
