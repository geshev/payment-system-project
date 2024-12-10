package com.example.payment.data.dto.transaction;

import com.example.payment.data.model.transaction.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionRequest(@NotNull TransactionType type, UUID uuid, @NotBlank String customerEmail,
                                 @NotBlank String customerPhone, @NotBlank String referenceId,
                                 @DecimalMin(value = "0", inclusive = false) BigDecimal amount) {
}
