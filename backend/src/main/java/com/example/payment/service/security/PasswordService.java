package com.example.payment.service.security;

import com.example.payment.data.mapper.UserDetailMapper;
import com.example.payment.data.model.Account;
import com.example.payment.data.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PasswordService implements UserDetailsPasswordService {

    private final UserDetailMapper userDetailMapper;
    private final AccountRepository accountRepository;

    @Autowired
    public PasswordService(UserDetailMapper userDetailMapper, AccountRepository accountRepository) {
        this.userDetailMapper = userDetailMapper;
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        Optional<Account> accountOpt = accountRepository.findByUsername(user.getUsername());
        if (accountOpt.isEmpty()) {
            throw new RuntimeException("Cannot update the password for " + user.getUsername()
                    + ", account not found");
        }
        Account account = accountOpt.get();
        account.setPassword(newPassword);
        return userDetailMapper.toUserDetails(account);
    }
}
