package com.example.payment.service;

import com.example.payment.data.dto.transaction.TransactionInfo;
import com.example.payment.data.dto.transaction.TransactionRequest;
import com.example.payment.data.mapper.TransactionMapper;
import com.example.payment.data.model.account.Account;
import com.example.payment.data.model.merchant.Merchant;
import com.example.payment.data.model.merchant.MerchantStatus;
import com.example.payment.data.model.transaction.*;
import com.example.payment.data.repo.TransactionRepository;
import com.example.payment.error.exception.DuplicateTransactionException;
import com.example.payment.error.exception.MerchantNotActiveException;
import com.example.payment.error.exception.MerchantNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            throws MerchantNotFoundException, MerchantNotActiveException, DuplicateTransactionException {
        Merchant merchant = verifyAndGetMerchant(account, true);

        ArrayList<Transaction> transactions = new ArrayList<>();
        Transaction transaction = transactionMapper.toTransaction(request);
        transactions.add(transaction);

        if (transactionRepository.existsByMerchantAndUuid(merchant, transaction.getUuid())) {
            throw new DuplicateTransactionException();
        }

        transaction.setMerchant(merchant);

        switch (transaction.getType()) {
            case AUTHORIZE -> transaction.setStatus(TransactionStatus.APPROVED);
            case CHARGE, REVERSAL -> {
                Optional<Transaction> auth = transactionRepository.findReferencedTransaction(
                        AuthorizeTransaction.class, merchant, transaction.getReferenceId());
                if (auth.isPresent() && auth.get().getStatus() == TransactionStatus.APPROVED) {
                    transaction.setStatus(TransactionStatus.APPROVED);
                    if (transaction.getType() == TransactionType.REVERSAL) {
                        auth.get().setStatus(TransactionStatus.REVERSED);
                        transactions.add(auth.get());
                    }
                } else {
                    transaction.setStatus(TransactionStatus.ERROR);
                }
            }
            case REFUND -> {
                Optional<Transaction> charge = transactionRepository.findReferencedTransaction(
                        ChargeTransaction.class, merchant, transaction.getReferenceId());
                if (charge.isPresent() && charge.get().getStatus() == TransactionStatus.APPROVED) {
                    transaction.setStatus(TransactionStatus.APPROVED);
                    charge.get().setStatus(TransactionStatus.REFUNDED);
                    transactions.add(charge.get());
                } else {
                    transaction.setStatus(TransactionStatus.ERROR);
                }
            }
            default -> transaction.setStatus(TransactionStatus.ERROR);
        }

        transactionRepository.saveAll(transactions);
    }

    public List<TransactionInfo> getTransactions(final Account account)
            throws MerchantNotFoundException, MerchantNotActiveException {
        Merchant merchant = verifyAndGetMerchant(account, false);
        return transactionRepository.findAllByMerchant(merchant).stream()
                .map(transactionMapper::toTransactionInfo).toList();
    }

    public boolean merchantHasTransactions(final Merchant merchant) {
        return transactionRepository.existsByMerchant(merchant);
    }

    private Merchant verifyAndGetMerchant(final Account account, final boolean requiresActive)
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
