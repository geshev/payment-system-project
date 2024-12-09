package com.example.payment.data.dto.merchant;

import com.example.payment.data.model.merchant.MerchantStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MerchantUpdate(@NotBlank String name, @NotBlank String description, @NotBlank String email,
                             @NotNull MerchantStatus status) {
}
