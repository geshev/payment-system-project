package com.example.payment.data.mapper;

import com.example.payment.data.dto.transaction.TransactionRequest;
import com.example.payment.data.model.transaction.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {TransactionFactory.class})
public interface TransactionMapper {

    Transaction toTransaction(TransactionRequest request);

    @AfterMapping
    default void mapAmount(TransactionRequest request, @MappingTarget Transaction transaction) {
        switch (transaction.getType()) {
            case AUTHORIZE -> ((AuthorizeTransaction) transaction).setAmount(request.amount());
            case CHARGE -> ((ChargeTransaction) transaction).setAmount(request.amount());
            case REFUND -> ((RefundTransaction) transaction).setAmount(request.amount());
            default -> {
                // REVERSAL does not require additional mapping
            }
        }
    }
}
