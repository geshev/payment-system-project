package com.example.payment.data.dto.transaction;

import com.example.payment.data.model.transaction.TransactionType;
import com.example.payment.validation.ValidTransactionRequest;

import java.math.BigDecimal;

@ValidTransactionRequest
public record TransactionRequest(TransactionType type, String uuid, String customerEmail, String customerPhone,
                                 String referenceId, BigDecimal amount) {
}
