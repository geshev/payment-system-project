package com.example.payment.data.mapper;

import com.example.payment.data.dto.MerchantCreation;
import com.example.payment.data.model.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MerchantMapper {

    @Mapping(target = "totalTransactionSum", expression = "java(new java.math.BigDecimal(\"0.00\"))")
    Merchant fromMerchantCreation(MerchantCreation creation);
}
