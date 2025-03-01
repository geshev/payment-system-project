package com.example.payment.error.advice;

import com.example.payment.error.exception.DuplicateTransactionException;
import com.example.payment.error.exception.MerchantNonDeletableException;
import com.example.payment.error.exception.MerchantNotActiveException;
import com.example.payment.error.exception.MerchantNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(value = {MerchantNotFoundException.class})
    ResponseEntity<Void> handleMerchantNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(value = {MerchantNotActiveException.class})
    ResponseEntity<Void> handleMerchantNotActive() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(value = {MerchantNonDeletableException.class})
    ResponseEntity<Void> handleMerchantNonDeletable() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(value = {DuplicateTransactionException.class})
    ResponseEntity<Void> handleDuplicateTransaction() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
