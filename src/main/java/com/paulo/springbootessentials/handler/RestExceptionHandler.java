package com.paulo.springbootessentials.handler;

import com.paulo.springbootessentials.exception.ObjectNotFoundException;
import com.paulo.springbootessentials.exception.details.BadRequestExceptionDetails;
import com.paulo.springbootessentials.exception.details.ExceptionDetails;
import com.paulo.springbootessentials.exception.details.ValidationExceptionDetails;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    protected ResponseEntity<Object> handleBadRequestException(ObjectNotFoundException exception, WebRequest request) {
        BadRequestExceptionDetails details = BadRequestExceptionDetails.builder()
                .title("Error: Somethings wrong on retrieving data.")
                .status(HttpStatus.BAD_REQUEST.value())
                .details(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return createResponseEntity(details, null, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String fields = exception.getBindingResult().getFieldErrors().stream().map(FieldError::getField).collect(Collectors.joining(","));
        String errors = exception.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
        ValidationExceptionDetails details = ValidationExceptionDetails.builder()
                .title("Error: Some fields can be invalid.")
                .exception(exception.getClass().getSimpleName() + ": " + exception.getClass().getName())
                .details(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .fields(fields)
                .errors(errors)
                .status(status.value())
                .build();
        return createResponseEntity(details, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception exception, @Nullable Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ExceptionDetails details = ExceptionDetails.builder()
                .title("Error: Please, check your documentation.")
                .exception(exception.getClass().getSimpleName() + ": " + exception.getClass().getName())
                .details(exception.getMessage())
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .build();
        return createResponseEntity(details, headers, status, request);
    }
}
