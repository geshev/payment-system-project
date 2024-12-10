package com.example.payment.service;

import com.example.payment.data.dto.transaction.TransactionRequest;
import com.example.payment.data.mapper.TransactionMapper;
import com.example.payment.data.model.account.Account;
import com.example.payment.data.model.merchant.Merchant;
import com.example.payment.data.model.transaction.Transaction;
import com.example.payment.data.model.transaction.TransactionStatus;
import com.example.payment.data.repo.TransactionRepository;
import com.example.payment.error.exception.MerchantNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransactionService {

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;

    public TransactionService(final TransactionMapper transactionMapper,
                              final TransactionRepository transactionRepository) {
        this.transactionMapper = transactionMapper;
        this.transactionRepository = transactionRepository;
    }

    public void processTransaction(final Account account, final TransactionRequest request)
            throws MerchantNotFoundException {
        Merchant merchant = account.getMerchant();
        if (merchant == null) {
            throw new MerchantNotFoundException();
        }

        Transaction transaction = transactionMapper.toTransaction(request);
        transaction.setStatus(TransactionStatus.ERROR);
        transaction.setMerchant(merchant);

        transactionRepository.save(transaction);
    }
}
