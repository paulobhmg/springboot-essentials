package com.paulo.springbootessentials.handler;

import com.paulo.springbootessentials.exception.ObjectNotFoundException;
import com.paulo.springbootessentials.exception.details.*;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
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
    protected ResponseEntity<Object> handleBadRequestException(ObjectNotFoundException exception, HttpHeaders headers, WebRequest request) {
        BadRequestExceptionDetails details = BadRequestExceptionDetails.builder()
                .title("Error: Somethings wrong on retrieving data.")
                .exception(exception.getClass().getName())
                .status(HttpStatus.BAD_REQUEST.value())
                .details(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return createResponseEntity(details, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception, HttpHeaders headers, WebRequest request) {
        AccessDeniedExceptionDetails details = AccessDeniedExceptionDetails.builder()
                .title("Access denied: Please check your permissions.")
                .exception(getExceptionName(exception))
                .status(HttpStatus.FORBIDDEN.value())
                .details(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return createResponseEntity(details, headers, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<Object> handleNullPointerException(NullPointerException exception, HttpHeaders headers, WebRequest request) {
        NullPointerExceptionDetails details = NullPointerExceptionDetails.builder()
                .title("Something is null. Please check the object values.")
                .exception(getExceptionName(exception))
                .status(HttpStatus.BAD_REQUEST.value())
                .details(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return createResponseEntity(details, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ClassNotFoundException.class)
    protected ResponseEntity<Object> handleClassNotFoundException(ClassNotFoundException exception, HttpHeaders headers, WebRequest request) {
        ClassNotFoundExceptionDetails details = ClassNotFoundExceptionDetails.builder()
                .title("Something is wrong when try to load class.")
                .exception(getExceptionName(exception))
                .status(HttpStatus.NOT_FOUND.value())
                .details(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return createResponseEntity(details, headers, HttpStatus.BAD_REQUEST, request);

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String fields = exception.getBindingResult().getFieldErrors().stream().map(FieldError::getField).collect(Collectors.joining(","));
        String errors = exception.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
        ValidationExceptionDetails details = ValidationExceptionDetails.builder()
                .title("Error: Some fields can be invalid.")
                .exception(getExceptionName(exception))
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
                .exception(getExceptionName(exception))
                .details(exception.getMessage())
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .build();
        return createResponseEntity(details, headers, status, request);
    }

    private String getExceptionName(Exception exception) {
        return String.format("%s: %s", exception.getClass().getSimpleName(), exception.getClass().getName());
    }
}
