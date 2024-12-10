package com.example.payment.data.dto.transaction;

import com.example.payment.data.model.transaction.TransactionStatus;
import com.example.payment.data.model.transaction.TransactionType;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TransactionInfo(TransactionType type, UUID uuid, TransactionStatus status, String customerEmail,
                              String customerPhone, String referenceId, BigDecimal amount) {
}
