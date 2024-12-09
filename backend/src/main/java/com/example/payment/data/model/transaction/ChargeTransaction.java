package com.example.payment.data.model.transaction;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("CHARGE")
public class ChargeTransaction extends Transaction {

    @Column(nullable = false)
    private BigDecimal amount;
}
