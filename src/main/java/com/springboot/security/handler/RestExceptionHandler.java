package com.springboot.security.handler;

import com.springboot.security.exception.BadRequestException;
import com.springboot.security.exception.BadRequestExceptionDetails;
import com.springboot.security.exception.ExceptionDetails;
import com.springboot.security.exception.ValidationExceptionDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handleBadRequestException(BadRequestException exception) {
        return new ResponseEntity<>(BadRequestExceptionDetails.builder()
                .title("Bad Request Exception")
                .status(HttpStatus.BAD_REQUEST.value())
                .details(exception.getMessage())
                .developerMessage(exception.getClass().getName())
                .timestamp(LocalDateTime.now())
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        final String field = exception.getFieldErrors().stream().map(FieldError::getField).collect(Collectors.joining(", "));
        final String fieldMessage = exception.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
        return new ResponseEntity<>(ValidationExceptionDetails.builder()
                .field(field)
                .title("Bad Request Exception")
                .status(HttpStatus.BAD_REQUEST.value())
                .details("Check the fields")
                .developerMessage(exception.getClass().getName())
                .timestamp(LocalDateTime.now())
                .build(),
                status);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .title("Bad Request Exception")
                        .status(statusCode.value())
                        .details(exception.getMessage())
                        .developerMessage(exception.getClass().getName())
                        .timestamp(LocalDateTime.now())
                        .build()
                , statusCode);
    }
}
