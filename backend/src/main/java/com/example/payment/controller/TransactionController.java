package com.example.payment.controller;

import com.example.payment.data.dto.transaction.TransactionInfo;
import com.example.payment.data.dto.transaction.TransactionRequest;
import com.example.payment.data.model.account.Account;
import com.example.payment.error.exception.DuplicateTransactionException;
import com.example.payment.error.exception.MerchantNotActiveException;
import com.example.payment.error.exception.MerchantNotFoundException;
import com.example.payment.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(final TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionInfo>> getTransactions(@AuthenticationPrincipal final Account account)
            throws MerchantNotFoundException, MerchantNotActiveException {
        return ResponseEntity.ok(transactionService.getTransactions(account));
    }

    @PostMapping
    public ResponseEntity<Void> processTransaction(@AuthenticationPrincipal final Account account,
                                                   @RequestBody @Valid final TransactionRequest request)
            throws MerchantNotFoundException, MerchantNotActiveException, DuplicateTransactionException {
        transactionService.processTransaction(account, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
