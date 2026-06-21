package com.wip.assetmanagementsystem.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

   
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> 
           handleResourceNotFoundException(
                ResourceNotFoundException ex,
                HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            "Resource Not Found",
            request.getRequestURI()
        );
        return new ResponseEntity<>(
            error, HttpStatus.NOT_FOUND);
    }

    
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> 
           handleBadRequestException(
                BadRequestException ex,
                HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            "Bad Request",
            request.getRequestURI()
        );
        return new ResponseEntity<>(
            error, HttpStatus.BAD_REQUEST);
    }

    
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> 
           handleAlreadyExistsException(
                AlreadyExistsException ex,
                HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            ex.getMessage(),
            "Already Exists",
            request.getRequestURI()
        );
        return new ResponseEntity<>(
            error, HttpStatus.CONFLICT);
    }

    
    @ExceptionHandler(
        MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> 
           handleValidationException(
                MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
          .getAllErrors()
          .forEach(error -> {
              String fieldName = 
                  ((FieldError) error).getField();
              String errorMessage = 
                  error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
          });

        return new ResponseEntity<>(
            errors, HttpStatus.BAD_REQUEST);
    }

   
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> 
           handleGlobalException(
                Exception ex,
                HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            ex.getMessage(),
            "Internal Server Error",
            request.getRequestURI()
        );
        return new ResponseEntity<>(
            error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}