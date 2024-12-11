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
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransactionService {

    private static final BigDecimal NEGATIVE_AMOUNT = BigDecimal.valueOf(-1L);

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final MerchantService merchantService;

    public TransactionService(final TransactionMapper transactionMapper,
                              final TransactionRepository transactionRepository,
                              @Lazy final MerchantService merchantService) {
        this.transactionMapper = transactionMapper;
        this.transactionRepository = transactionRepository;
        this.merchantService = merchantService;
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
        transaction.setCreated(LocalDateTime.now());

        switch (transaction.getType()) {
            case AUTHORIZE -> transaction.setStatus(TransactionStatus.APPROVED);
            case CHARGE, REVERSAL -> {
                Optional<Transaction> auth = transactionRepository.findReferencedTransaction(
                        AuthorizeTransaction.class, merchant, transaction.getReferenceId());
                if (auth.isPresent()) {
                    if (transaction.getType() == TransactionType.REVERSAL) {
                        transaction.setStatus(TransactionStatus.APPROVED);
                        auth.get().setStatus(TransactionStatus.REVERSED);
                        transactions.add(auth.get());
                    } else {
                        // Check if we already have a charge for the AUTHORIZE transaction
                        Optional<Transaction> charge = transactionRepository.findReferencedTransaction(
                                ChargeTransaction.class, merchant, transaction.getReferenceId());
                        if (charge.isPresent() && charge.get().getStatus() == TransactionStatus.APPROVED) {
                            transaction.setStatus(TransactionStatus.ERROR);
                        } else {
                            ChargeTransaction chargeTransaction = (ChargeTransaction) transaction;
                            merchantService.updateMerchantTotalSum(merchant, chargeTransaction.getAmount());
                        }
                    }
                } else {
                    transaction.setStatus(TransactionStatus.ERROR);
                }
            }
            case REFUND -> {
                Optional<Transaction> charge = transactionRepository.findReferencedTransaction(
                        ChargeTransaction.class, merchant, transaction.getReferenceId());
                if (charge.isPresent()) {
                    ChargeTransaction chargeTransaction = (ChargeTransaction) charge.get();
                    RefundTransaction refundTransaction = (RefundTransaction) transaction;
                    if (chargeTransaction.getAmount().equals(refundTransaction.getAmount())) {
                        transaction.setStatus(TransactionStatus.APPROVED);
                        chargeTransaction.setStatus(TransactionStatus.REFUNDED);
                        transactions.add(chargeTransaction);

                        merchantService.updateMerchantTotalSum(merchant,
                                refundTransaction.getAmount().multiply(NEGATIVE_AMOUNT));
                    } else {
                        transaction.setStatus(TransactionStatus.ERROR);
                    }
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

    @Scheduled(cron = "0 0 * * * *")
    void clearOldTransactions() {
        transactionRepository.deleteAllByCreatedBefore(LocalDateTime.now().minusHours(1));
    }
}
