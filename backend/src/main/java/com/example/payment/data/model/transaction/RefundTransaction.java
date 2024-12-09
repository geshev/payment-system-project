package com.example.payment.data.model.transaction;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("REFUND")
public class RefundTransaction extends Transaction {

    @Column(nullable = false)
    private BigDecimal amount;
}
