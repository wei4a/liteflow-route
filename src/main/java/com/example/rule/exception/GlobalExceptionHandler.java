package com.example.rule.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // 处理 RuntimeException 异常
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("RuntimeException occurred while processing request {} {}",
                request.getMethod(), request.getRequestURL(), e);
        String errorMessage = "A runtime error occurred: " + e.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    // 处理 NullPointerException 异常
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        log.error("NullPointerException occurred while processing request {} {}",
                request.getMethod(), request.getRequestURL(), e);
        String errorMessage = "A null pointer error occurred: " + e.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    // 处理 IllegalArgumentException 异常
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.error("IllegalArgumentException occurred while processing request {} {}",
                request.getMethod(), request.getRequestURL(), e);
        String errorMessage = "An illegal argument error occurred: " + e.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    // 处理通用的 Exception 异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e, HttpServletRequest request) {
        log.error("General exception occurred while processing request {} {}",
                request.getMethod(), request.getRequestURL(), e);
        String errorMessage = "An unexpected error occurred: " + e.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
}
