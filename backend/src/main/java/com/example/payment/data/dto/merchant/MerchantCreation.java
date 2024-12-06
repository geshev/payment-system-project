package com.example.payment.data.dto.merchant;

import com.example.payment.data.model.MerchantStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MerchantCreation {

    private String username;
    private String name;
    private String description;
    private String email;
    private MerchantStatus status;
}
