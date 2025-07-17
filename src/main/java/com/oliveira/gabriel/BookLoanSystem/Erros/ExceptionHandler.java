package com.oliveira.gabriel.BookLoanSystem.Erros;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;



@ControllerAdvice
public class ExceptionHandler {


    @org.springframework.web.bind.annotation.ExceptionHandler(ContentNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(ContentNotFoundException ex, HttpServletRequest request){

        Map<String, Object> error = new LinkedHashMap<>();

        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.NO_CONTENT.value());
        error.put("error", "no find");
        error.put("message", ex.getMessage());
        error.put("Path", request.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundBook(BookNotFoundException ex, HttpServletRequest request){

        Map<String, Object> error = new LinkedHashMap<>();

        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.NO_CONTENT.value());
        error.put("error", "no find");
        error.put("message", ex.getMessage());
        error.put("Path", request.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }

}
