package com.example.payment.data.mapper;

import com.example.payment.data.dto.transaction.TransactionInfo;
import com.example.payment.data.dto.transaction.TransactionRequest;
import com.example.payment.data.model.transaction.*;
import org.mapstruct.*;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", uses = {TransactionFactory.class})
public interface TransactionMapper {

    @Mapping(target = "amount", expression = "java(amountFromTransaction(transaction))")
    TransactionInfo toTransactionInfo(Transaction transaction);

    default BigDecimal amountFromTransaction(Transaction transaction) {
        return switch (transaction.getType()) {
            case AUTHORIZE -> ((AuthorizeTransaction) transaction).getAmount();
            case CHARGE -> ((ChargeTransaction) transaction).getAmount();
            case REFUND -> ((RefundTransaction) transaction).getAmount();
            default -> null;
        };
    }

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
