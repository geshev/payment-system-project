package com.example.payment.service;

import com.example.payment.data.model.account.Account;
import com.example.payment.data.model.merchant.Merchant;
import com.example.payment.data.repo.AccountRepository;
import com.example.payment.util.CSVUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public AccountService(final PasswordEncoder passwordEncoder,
                          final AccountRepository accountRepository) throws IOException {
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        loadAccounts();
    }

    private void loadAccounts() throws IOException {
        List<Account> accounts = CSVUtils.loadCSV("data/accounts.csv", Account.class);
        accounts.forEach(account -> {
            if (!accountRepository.existsByUsername(account.getUsername())) {
                account.setPassword(passwordEncoder.encode(account.getPassword()));
                account.setEnabled(true);
                accountRepository.save(account);
            }
        });
    }

    public boolean accountExists(final String username) {
        return accountRepository.existsByUsername(username);
    }

    public String getAccountRole(final String username) {
        Account account = accountRepository.findByUsername(username).orElseThrow();
        return account.getRole().name();
    }

    public void removeMerchantFromAccount(final Merchant merchant) {
        Optional<Account> accountOpt = accountRepository.findByMerchant(merchant);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            account.setMerchant(null);
            accountRepository.save(account);
        }
    }

    void addMerchantToAccount(final String username, final Merchant merchant) {
        Account account = accountRepository.findByUsername(username).orElseThrow();
        account.setMerchant(merchant);
        accountRepository.save(account);
    }
}
