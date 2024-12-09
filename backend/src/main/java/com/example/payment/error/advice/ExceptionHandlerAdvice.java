package com.example.payment.error.advice;

import com.example.payment.error.exception.MerchantNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(value = {MerchantNotFoundException.class})
    ResponseEntity<Void> handleTeacherNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
