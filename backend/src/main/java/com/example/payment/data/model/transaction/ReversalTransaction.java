package com.example.payment.data.model.transaction;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("REVERSAL")
public class ReversalTransaction extends Transaction {
}
