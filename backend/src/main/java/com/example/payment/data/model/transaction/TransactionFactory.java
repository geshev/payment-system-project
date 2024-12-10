package com.example.payment.data.model.transaction;

import com.example.payment.data.dto.transaction.TransactionRequest;
import org.mapstruct.ObjectFactory;
import org.springframework.stereotype.Component;

@Component
public class TransactionFactory {

    @ObjectFactory
    public Transaction createTransaction(final TransactionRequest request) {
        return switch (request.type()) {
            case AUTHORIZE -> new AuthorizeTransaction();
            case CHARGE -> new ChargeTransaction();
            case REFUND -> new RefundTransaction();
            case REVERSAL -> new ReversalTransaction();
        };
    }
}
