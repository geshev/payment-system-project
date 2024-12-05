package com.example.payment.data.mapper;

import com.example.payment.data.model.Account;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserDetailMapper {

    public UserDetails toUserDetails(Account account) {
        return User.withUsername(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRole().name())
                .build();
    }
}
