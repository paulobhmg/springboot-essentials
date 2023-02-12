package com.paulo.springbootessentials.handler;

import com.paulo.springbootessentials.exception.AnimeNotFoundException;
import com.paulo.springbootessentials.exception.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(AnimeNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handlerException(AnimeNotFoundException exception) {
        return new ResponseEntity<>(
                ExceptionDetails.builder()
                .title(exception.getClass().getSimpleName() + ": error getting anime")
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .developerMessage(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.BAD_REQUEST);
    }
}
