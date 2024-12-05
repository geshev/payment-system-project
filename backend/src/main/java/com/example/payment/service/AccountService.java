package com.example.payment.service;

import com.example.payment.data.model.Account;
import com.example.payment.data.model.Role;
import com.example.payment.data.repo.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public AccountService(PasswordEncoder passwordEncoder, AccountRepository accountRepository) {
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        createInitialAccounts();
    }

    private void createInitialAccounts() {
        Account admin = new Account();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setEnabled(true);
        admin.setRole(Role.ADMIN);
        Account merchant = new Account();
        merchant.setUsername("merchant");
        merchant.setPassword(passwordEncoder.encode("merchant"));
        merchant.setEnabled(true);
        merchant.setRole(Role.MERCHANT);

        if (accountRepository.findByUsername(admin.getUsername()).isEmpty()) {
            accountRepository.save(admin);
        }
        if (accountRepository.findByUsername(merchant.getUsername()).isEmpty()) {
            accountRepository.save(merchant);
        }
    }

    public String getAccountRole(String username) {
        Account account = accountRepository.findByUsername(username).orElseThrow();
        return account.getRole().name();
    }
}
