package com.example.payment.service;

import com.example.payment.data.dto.transaction.TransactionInfo;
import com.example.payment.data.dto.transaction.TransactionRequest;
import com.example.payment.data.mapper.TransactionMapper;
import com.example.payment.data.model.account.Account;
import com.example.payment.data.model.merchant.Merchant;
import com.example.payment.data.model.merchant.MerchantStatus;
import com.example.payment.data.model.transaction.Transaction;
import com.example.payment.data.model.transaction.TransactionStatus;
import com.example.payment.data.repo.TransactionRepository;
import com.example.payment.error.exception.MerchantNotActiveException;
import com.example.payment.error.exception.MerchantNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            throws MerchantNotFoundException, MerchantNotActiveException {
        Merchant merchant = verifyAndGetMerchant(account, true);

        Transaction transaction = transactionMapper.toTransaction(request);
        transaction.setStatus(TransactionStatus.ERROR);
        transaction.setMerchant(merchant);

        transactionRepository.save(transaction);
    }

    public List<TransactionInfo> getTransactions(Account account)
            throws MerchantNotFoundException, MerchantNotActiveException {
        Merchant merchant = verifyAndGetMerchant(account, false);
        return transactionRepository.findAllByMerchant(merchant).stream()
                .map(transactionMapper::toTransactionInfo).toList();
    }

    private Merchant verifyAndGetMerchant(Account account, boolean requiresActive)
            throws MerchantNotFoundException, MerchantNotActiveException {
        Merchant merchant = account.getMerchant();
        if (merchant == null) {
            throw new MerchantNotFoundException();
        } else if (requiresActive && merchant.getStatus() == MerchantStatus.INACTIVE) {
            throw new MerchantNotActiveException();
        }
        return merchant;
    }
}
