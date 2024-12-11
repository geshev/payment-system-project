package com.example.payment.data.mapper;

import com.example.payment.data.dto.transaction.TransactionInfo;
import com.example.payment.data.dto.transaction.TransactionRequest;
import com.example.payment.data.mapper.TransactionMapperImpl;
import com.example.payment.data.model.transaction.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TransactionMapperTest {

    private static final UUID TEST_UUID = UUID.randomUUID();
    private static final TransactionStatus TEST_STATUS = TransactionStatus.APPROVED;
    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_PHONE = "+19090909090";
    private static final String TEST_REFERENCE_ID = "090909";
    private static final BigDecimal TEST_AMOUNT = new BigDecimal("10.10");

    private final TransactionMapperImpl testMapper;

    @Autowired
    public TransactionMapperTest(final TransactionMapperImpl testMapper) {
        this.testMapper = testMapper;
    }

    @Test
    void testAuthorizeTransaction() {
        Transaction transaction = testMapper.toTransaction(createTransactionRequest(TransactionType.AUTHORIZE));
        assertThat(transaction.getType()).isEqualTo(TransactionType.AUTHORIZE);
        assertThat(transaction.getUuid()).isEqualTo(TEST_UUID);
        assertThat(transaction.getCustomerEmail()).isEqualTo(TEST_EMAIL);
        assertThat(transaction.getCustomerPhone()).isEqualTo(TEST_PHONE);
        assertThat(transaction.getReferenceId()).isEqualTo(TEST_REFERENCE_ID);
        assertThat(((AuthorizeTransaction) transaction).getAmount()).isEqualTo(TEST_AMOUNT);

        transaction.setStatus(TEST_STATUS);
        TransactionInfo info = testMapper.toTransactionInfo(transaction);
        assertThat(info.type()).isEqualTo(TransactionType.AUTHORIZE);
        assertThat(info.uuid()).isEqualTo(TEST_UUID);
        assertThat(info.status()).isEqualTo(TEST_STATUS);
        assertThat(info.customerEmail()).isEqualTo(TEST_EMAIL);
        assertThat(info.customerPhone()).isEqualTo(TEST_PHONE);
        assertThat(info.referenceId()).isEqualTo(TEST_REFERENCE_ID);
        assertThat(info.amount()).isEqualTo(TEST_AMOUNT);
    }

    @Test
    void testChargeTransaction() {
        Transaction transaction = testMapper.toTransaction(createTransactionRequest(TransactionType.CHARGE));
        assertThat(transaction.getType()).isEqualTo(TransactionType.CHARGE);
        assertThat(transaction.getUuid()).isEqualTo(TEST_UUID);
        assertThat(transaction.getCustomerEmail()).isEqualTo(TEST_EMAIL);
        assertThat(transaction.getCustomerPhone()).isEqualTo(TEST_PHONE);
        assertThat(transaction.getReferenceId()).isEqualTo(TEST_REFERENCE_ID);
        assertThat(((ChargeTransaction) transaction).getAmount()).isEqualTo(TEST_AMOUNT);

        transaction.setStatus(TEST_STATUS);
        TransactionInfo info = testMapper.toTransactionInfo(transaction);
        assertThat(info.type()).isEqualTo(TransactionType.CHARGE);
        assertThat(info.uuid()).isEqualTo(TEST_UUID);
        assertThat(info.status()).isEqualTo(TEST_STATUS);
        assertThat(info.customerEmail()).isEqualTo(TEST_EMAIL);
        assertThat(info.customerPhone()).isEqualTo(TEST_PHONE);
        assertThat(info.referenceId()).isEqualTo(TEST_REFERENCE_ID);
        assertThat(info.amount()).isEqualTo(TEST_AMOUNT);
    }

    @Test
    void testRefundTransaction() {
        Transaction transaction = testMapper.toTransaction(createTransactionRequest(TransactionType.REFUND));
        assertThat(transaction.getType()).isEqualTo(TransactionType.REFUND);
        assertThat(transaction.getUuid()).isEqualTo(TEST_UUID);
        assertThat(transaction.getCustomerEmail()).isEqualTo(TEST_EMAIL);
        assertThat(transaction.getCustomerPhone()).isEqualTo(TEST_PHONE);
        assertThat(transaction.getReferenceId()).isEqualTo(TEST_REFERENCE_ID);
        assertThat(((RefundTransaction) transaction).getAmount()).isEqualTo(TEST_AMOUNT);

        transaction.setStatus(TEST_STATUS);
        TransactionInfo info = testMapper.toTransactionInfo(transaction);
        assertThat(info.type()).isEqualTo(TransactionType.REFUND);
        assertThat(info.uuid()).isEqualTo(TEST_UUID);
        assertThat(info.status()).isEqualTo(TEST_STATUS);
        assertThat(info.customerEmail()).isEqualTo(TEST_EMAIL);
        assertThat(info.customerPhone()).isEqualTo(TEST_PHONE);
        assertThat(info.referenceId()).isEqualTo(TEST_REFERENCE_ID);
        assertThat(info.amount()).isEqualTo(TEST_AMOUNT);
    }

    @Test
    void testReversalTransaction() {
        Transaction transaction = testMapper.toTransaction(createTransactionRequest(TransactionType.REVERSAL));
        assertThat(transaction.getType()).isEqualTo(TransactionType.REVERSAL);
        assertThat(transaction.getUuid()).isEqualTo(TEST_UUID);
        assertThat(transaction.getCustomerEmail()).isEqualTo(TEST_EMAIL);
        assertThat(transaction.getCustomerPhone()).isEqualTo(TEST_PHONE);
        assertThat(transaction.getReferenceId()).isEqualTo(TEST_REFERENCE_ID);

        transaction.setStatus(TEST_STATUS);
        TransactionInfo info = testMapper.toTransactionInfo(transaction);
        assertThat(info.type()).isEqualTo(TransactionType.REVERSAL);
        assertThat(info.uuid()).isEqualTo(TEST_UUID);
        assertThat(info.status()).isEqualTo(TEST_STATUS);
        assertThat(info.customerEmail()).isEqualTo(TEST_EMAIL);
        assertThat(info.customerPhone()).isEqualTo(TEST_PHONE);
        assertThat(info.referenceId()).isEqualTo(TEST_REFERENCE_ID);
        assertThat(info.amount()).isNull();
    }

    private TransactionRequest createTransactionRequest(final TransactionType type) {
        return new TransactionRequest(type, TEST_UUID.toString(), TEST_EMAIL, TEST_PHONE, TEST_REFERENCE_ID, TEST_AMOUNT);
    }
}
