package com.example.payment.data.mapper;

import com.example.payment.data.dto.merchant.MerchantCreation;
import com.example.payment.data.dto.merchant.MerchantInfo;
import com.example.payment.data.model.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MerchantMapper {

    MerchantInfo toInfo(Merchant merchant);

    @Mapping(target = "totalTransactionSum", expression = "java(new java.math.BigDecimal(\"0.00\"))")
    Merchant fromMerchantCreation(MerchantCreation creation);
}
