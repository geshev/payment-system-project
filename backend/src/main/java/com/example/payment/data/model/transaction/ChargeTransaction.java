package com.example.payment.data.model.transaction;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@DiscriminatorValue("CHARGE")
public class ChargeTransaction extends Transaction {

    @Column(nullable = false)
    private BigDecimal amount;

    @Override
    public TransactionType getType() {
        return TransactionType.CHARGE;
    }
}
