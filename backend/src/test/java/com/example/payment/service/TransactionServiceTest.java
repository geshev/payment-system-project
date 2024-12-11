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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    private static final Merchant TEST_MERCHANT = new Merchant();
    private static final Merchant INACTIVE_MERCHANT = new Merchant();

    static {
        INACTIVE_MERCHANT.setStatus(MerchantStatus.INACTIVE);
    }

    private static final Account EMPTY_ACCOUNT = new Account();
    private static final Account INACTIVE_MERCHANT_ACCOUNT = new Account();
    private static final Account TEST_ACCOUNT = new Account();

    static {
        INACTIVE_MERCHANT_ACCOUNT.setMerchant(INACTIVE_MERCHANT);

        TEST_ACCOUNT.setMerchant(TEST_MERCHANT);
    }

    private static final UUID TEST_UUID = UUID.randomUUID();
    private static final TransactionStatus TEST_STATUS = TransactionStatus.APPROVED;
    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_PHONE = "+19090909090";
    private static final String TEST_REFERENCE_ID = "090909";
    private static final BigDecimal TEST_AMOUNT = new BigDecimal("10.10");
    private static final BigDecimal TEST_NEGATIVE_AMOUNT = new BigDecimal("-10.10");

    private static final Long TEST_AUTHORIZE_TRANSACTION_ID = 1L;
    private static final AuthorizeTransaction TEST_AUTHORIZE_TRANSACTION = new AuthorizeTransaction();
    private static final TransactionRequest TEST_AUTHORIZE_REQUEST =
            new TransactionRequest(TransactionType.AUTHORIZE, TEST_UUID.toString(), TEST_EMAIL, TEST_PHONE,
                    TEST_REFERENCE_ID, TEST_AMOUNT);
    private static final TransactionInfo TEST_AUTHORIZE_INFO =
            new TransactionInfo(TransactionType.AUTHORIZE, TEST_UUID, TEST_STATUS, TEST_EMAIL, TEST_PHONE,
                    TEST_REFERENCE_ID, TEST_AMOUNT);


    private static final Long TEST_CHARGE_TRANSACTION_ID = 2L;
    private static final ChargeTransaction TEST_CHARGE_TRANSACTION = new ChargeTransaction();
    private static final TransactionRequest TEST_CHARGE_REQUEST =
            new TransactionRequest(TransactionType.CHARGE, TEST_UUID.toString(), TEST_EMAIL, TEST_PHONE,
                    TEST_REFERENCE_ID, TEST_AMOUNT);
    private static final TransactionInfo TEST_CHARGE_INFO =
            new TransactionInfo(TransactionType.CHARGE, TEST_UUID, TEST_STATUS, TEST_EMAIL, TEST_PHONE,
                    TEST_REFERENCE_ID, TEST_AMOUNT);

    private static final Long TEST_REFUND_TRANSACTION_ID = 3L;
    private static final RefundTransaction TEST_REFUND_TRANSACTION = new RefundTransaction();
    private static final TransactionRequest TEST_REFUND_REQUEST =
            new TransactionRequest(TransactionType.REFUND, TEST_UUID.toString(), TEST_EMAIL, TEST_PHONE,
                    TEST_REFERENCE_ID, TEST_AMOUNT);
    private static final TransactionInfo TEST_REFUND_INFO =
            new TransactionInfo(TransactionType.REFUND, TEST_UUID, TEST_STATUS, TEST_EMAIL, TEST_PHONE,
                    TEST_REFERENCE_ID, TEST_AMOUNT);

    private static final Long TEST_REVERSAL_TRANSACTION_ID = 4L;
    private static final ReversalTransaction TEST_REVERSAL_TRANSACTION = new ReversalTransaction();
    private static final TransactionRequest TEST_REVERSAL_REQUEST =
            new TransactionRequest(TransactionType.REVERSAL, TEST_UUID.toString(), TEST_EMAIL, TEST_PHONE,
                    TEST_REFERENCE_ID, null);
    private static final TransactionInfo TEST_REVERSAL_INFO =
            new TransactionInfo(TransactionType.REVERSAL, TEST_UUID, TEST_STATUS, TEST_EMAIL, TEST_PHONE,
                    TEST_REFERENCE_ID, null);

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private MerchantService merchantService;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void resetTestTransactions() {
        TEST_AUTHORIZE_TRANSACTION.setId(TEST_AUTHORIZE_TRANSACTION_ID);
        TEST_AUTHORIZE_TRANSACTION.setUuid(TEST_UUID);
        TEST_AUTHORIZE_TRANSACTION.setStatus(TransactionStatus.APPROVED);
        TEST_AUTHORIZE_TRANSACTION.setCustomerEmail(TEST_EMAIL);
        TEST_AUTHORIZE_TRANSACTION.setCustomerPhone(TEST_PHONE);
        TEST_AUTHORIZE_TRANSACTION.setReferenceId(TEST_REFERENCE_ID);
        TEST_AUTHORIZE_TRANSACTION.setAmount(TEST_AMOUNT);

        TEST_CHARGE_TRANSACTION.setId(TEST_CHARGE_TRANSACTION_ID);
        TEST_CHARGE_TRANSACTION.setUuid(TEST_UUID);
        TEST_CHARGE_TRANSACTION.setStatus(TransactionStatus.APPROVED);
        TEST_CHARGE_TRANSACTION.setReferenceId(TEST_REFERENCE_ID);
        TEST_CHARGE_TRANSACTION.setAmount(TEST_AMOUNT);

        TEST_REFUND_TRANSACTION.setId(TEST_REFUND_TRANSACTION_ID);
        TEST_REFUND_TRANSACTION.setUuid(TEST_UUID);
        TEST_REFUND_TRANSACTION.setReferenceId(TEST_REFERENCE_ID);
        TEST_REFUND_TRANSACTION.setAmount(TEST_AMOUNT);

        TEST_REVERSAL_TRANSACTION.setId(TEST_REVERSAL_TRANSACTION_ID);
        TEST_REVERSAL_TRANSACTION.setUuid(TEST_UUID);
        TEST_REVERSAL_TRANSACTION.setReferenceId(TEST_REFERENCE_ID);
    }

    @Test
    void testAuthorizeTransactionProcess() throws MerchantNotFoundException, MerchantNotActiveException,
            DuplicateTransactionException {
        when(transactionMapper.toTransaction(TEST_AUTHORIZE_REQUEST)).thenReturn(TEST_AUTHORIZE_TRANSACTION);

        transactionService.processTransaction(TEST_ACCOUNT, TEST_AUTHORIZE_REQUEST);

        verify(transactionMapper, times(1)).toTransaction(TEST_AUTHORIZE_REQUEST);
        verify(transactionRepository, times(1)).saveAll(List.of(TEST_AUTHORIZE_TRANSACTION));
        verify(transactionRepository, times(1))
                .existsByMerchantAndUuid(TEST_MERCHANT, TEST_UUID);
    }

    @Test
    void testChargeTransactionProcess() throws MerchantNotFoundException, MerchantNotActiveException,
            DuplicateTransactionException {
        when(transactionMapper.toTransaction(TEST_CHARGE_REQUEST)).thenReturn(TEST_CHARGE_TRANSACTION);
        when(transactionRepository.findReferencedTransaction(AuthorizeTransaction.class,
                TEST_MERCHANT, TEST_REFERENCE_ID)).thenReturn(Optional.of(TEST_AUTHORIZE_TRANSACTION));

        transactionService.processTransaction(TEST_ACCOUNT, TEST_CHARGE_REQUEST);

        verify(transactionMapper, times(1)).toTransaction(TEST_CHARGE_REQUEST);
        verify(merchantService, times(1)).updateMerchantTotalSum(
                TEST_MERCHANT, TEST_AMOUNT);
        verify(transactionRepository, times(1)).saveAll(
                List.of(TEST_CHARGE_TRANSACTION));
        verify(transactionRepository, times(1))
                .existsByMerchantAndUuid(TEST_MERCHANT, TEST_UUID);
        verify(transactionRepository, times(1)).findReferencedTransaction(
                AuthorizeTransaction.class, TEST_MERCHANT, TEST_REFERENCE_ID);
        verify(transactionRepository, times(1)).findReferencedTransaction(
                ChargeTransaction.class, TEST_MERCHANT, TEST_REFERENCE_ID);
    }

    @Test
    void testDoubleChargeTransactionProcess() throws MerchantNotFoundException, MerchantNotActiveException,
            DuplicateTransactionException {
        when(transactionMapper.toTransaction(TEST_CHARGE_REQUEST)).thenReturn(TEST_CHARGE_TRANSACTION);
        when(transactionRepository.findReferencedTransaction(AuthorizeTransaction.class,
                TEST_MERCHANT, TEST_REFERENCE_ID)).thenReturn(Optional.of(TEST_AUTHORIZE_TRANSACTION));
        when(transactionRepository.findReferencedTransaction(ChargeTransaction.class,
                TEST_MERCHANT, TEST_REFERENCE_ID)).thenReturn(Optional.of(TEST_CHARGE_TRANSACTION));

        transactionService.processTransaction(TEST_ACCOUNT, TEST_CHARGE_REQUEST);

        verify(transactionMapper, times(1)).toTransaction(TEST_CHARGE_REQUEST);
        verify(merchantService, never()).updateMerchantTotalSum(TEST_MERCHANT, TEST_AMOUNT);
    }

    @Test
    void testChargeTransactionProcessNoAuthorize() throws MerchantNotFoundException, MerchantNotActiveException,
            DuplicateTransactionException {
        when(transactionMapper.toTransaction(TEST_CHARGE_REQUEST)).thenReturn(TEST_CHARGE_TRANSACTION);
        when(transactionRepository.findReferencedTransaction(AuthorizeTransaction.class,
                TEST_MERCHANT, TEST_REFERENCE_ID)).thenReturn(Optional.empty());

        transactionService.processTransaction(TEST_ACCOUNT, TEST_CHARGE_REQUEST);

        verify(merchantService, never()).updateMerchantTotalSum(TEST_MERCHANT, TEST_AMOUNT);
        verify(transactionRepository, times(1)).saveAll(
                List.of(TEST_CHARGE_TRANSACTION));
    }

    @Test
    void testRefundTransactionProcess() throws MerchantNotFoundException, MerchantNotActiveException,
            DuplicateTransactionException {
        when(transactionMapper.toTransaction(TEST_REFUND_REQUEST)).thenReturn(TEST_REFUND_TRANSACTION);
        when(transactionRepository.findReferencedTransaction(ChargeTransaction.class,
                TEST_MERCHANT, TEST_REFERENCE_ID)).thenReturn(Optional.of(TEST_CHARGE_TRANSACTION));

        transactionService.processTransaction(TEST_ACCOUNT, TEST_REFUND_REQUEST);

        verify(transactionMapper, times(1)).toTransaction(TEST_REFUND_REQUEST);
        verify(merchantService, times(1)).updateMerchantTotalSum(
                TEST_MERCHANT, TEST_NEGATIVE_AMOUNT);
        verify(transactionRepository, times(1)).saveAll(
                List.of(TEST_REFUND_TRANSACTION, TEST_CHARGE_TRANSACTION));
        verify(transactionRepository, times(1))
                .existsByMerchantAndUuid(TEST_MERCHANT, TEST_UUID);
        verify(transactionRepository, times(1)).findReferencedTransaction(
                ChargeTransaction.class, TEST_MERCHANT, TEST_REFERENCE_ID);
    }

    @Test
    void testRefundTransactionProcessNoCharge() throws MerchantNotFoundException, MerchantNotActiveException,
            DuplicateTransactionException {
        when(transactionMapper.toTransaction(TEST_REFUND_REQUEST)).thenReturn(TEST_REFUND_TRANSACTION);
        when(transactionRepository.findReferencedTransaction(ChargeTransaction.class,
                TEST_MERCHANT, TEST_REFERENCE_ID)).thenReturn(Optional.empty());

        transactionService.processTransaction(TEST_ACCOUNT, TEST_REFUND_REQUEST);

        verify(merchantService, never()).updateMerchantTotalSum(TEST_MERCHANT, TEST_NEGATIVE_AMOUNT);
        verify(transactionRepository, times(1)).saveAll(
                List.of(TEST_REFUND_TRANSACTION));
    }

    @Test
    void testReversalTransactionProcess() throws MerchantNotFoundException, MerchantNotActiveException,
            DuplicateTransactionException {
        when(transactionMapper.toTransaction(TEST_REVERSAL_REQUEST)).thenReturn(TEST_REVERSAL_TRANSACTION);
        when(transactionRepository.findReferencedTransaction(AuthorizeTransaction.class,
                TEST_MERCHANT, TEST_REFERENCE_ID)).thenReturn(Optional.of(TEST_AUTHORIZE_TRANSACTION));

        transactionService.processTransaction(TEST_ACCOUNT, TEST_REVERSAL_REQUEST);

        verify(transactionMapper, times(1)).toTransaction(TEST_REVERSAL_REQUEST);
        verify(transactionRepository, times(1)).saveAll(
                List.of(TEST_REVERSAL_TRANSACTION, TEST_AUTHORIZE_TRANSACTION));
        verify(transactionRepository, times(1))
                .existsByMerchantAndUuid(TEST_MERCHANT, TEST_UUID);
        verify(transactionRepository, times(1)).findReferencedTransaction(
                AuthorizeTransaction.class, TEST_MERCHANT, TEST_REFERENCE_ID);
    }

    @Test
    void testTransactionProcessNoMerchant() {
        assertThrows(MerchantNotFoundException.class, () ->
                transactionService.processTransaction(EMPTY_ACCOUNT, TEST_AUTHORIZE_REQUEST));
    }

    @Test
    void testTransactionProcessMerchantNotActive() {
        assertThrows(MerchantNotActiveException.class, () ->
                transactionService.processTransaction(INACTIVE_MERCHANT_ACCOUNT, TEST_AUTHORIZE_REQUEST));
    }

    @Test
    void testGetTransactions() throws MerchantNotFoundException, MerchantNotActiveException {
        List<Transaction> transactions =
                List.of(TEST_AUTHORIZE_TRANSACTION, TEST_CHARGE_TRANSACTION, TEST_REFUND_TRANSACTION);
        when(transactionRepository.findAllByMerchant(TEST_MERCHANT)).thenReturn(transactions);
        when(transactionMapper.toTransactionInfo(TEST_AUTHORIZE_TRANSACTION))
                .thenReturn(TEST_AUTHORIZE_INFO);
        when(transactionMapper.toTransactionInfo(TEST_CHARGE_TRANSACTION))
                .thenReturn(TEST_CHARGE_INFO);
        when(transactionMapper.toTransactionInfo(TEST_REFUND_TRANSACTION))
                .thenReturn(TEST_REFUND_INFO);

        List<TransactionInfo> result = transactionService.getTransactions(TEST_ACCOUNT);

        assertThat(result.size()).isEqualTo(transactions.size());
        assertThat(result.getFirst().type()).isEqualTo(TransactionType.AUTHORIZE);
        assertThat(result.getLast().type()).isEqualTo(TransactionType.REFUND);

        verify(transactionRepository, times(1)).findAllByMerchant(TEST_MERCHANT);
    }

    @Test
    void testGetTransactionsMerchantNotFound() {
        assertThrows(MerchantNotFoundException.class, () ->
                transactionService.getTransactions(EMPTY_ACCOUNT));
    }

    @Test
    void testTransactionConflict() {
        when(transactionMapper.toTransaction(TEST_AUTHORIZE_REQUEST)).thenReturn(TEST_AUTHORIZE_TRANSACTION);
        when(transactionRepository.existsByMerchantAndUuid(TEST_MERCHANT, TEST_UUID)).thenReturn(true);

        assertThrows(DuplicateTransactionException.class, () ->
                transactionService.processTransaction(TEST_ACCOUNT, TEST_AUTHORIZE_REQUEST));
    }
}
