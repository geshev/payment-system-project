package com.example.payment.data.dto.merchant;

import com.example.payment.data.model.merchant.MerchantStatus;

import java.math.BigDecimal;

public record MerchantInfo(String name, String description, String email, MerchantStatus status,
                           BigDecimal totalTransactionSum) {
}
