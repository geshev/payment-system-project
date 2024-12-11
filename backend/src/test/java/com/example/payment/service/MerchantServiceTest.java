package com.example.payment.service;

import com.example.payment.data.dto.merchant.MerchantInfo;
import com.example.payment.data.dto.merchant.MerchantUpdate;
import com.example.payment.data.mapper.MerchantMapper;
import com.example.payment.data.model.merchant.Merchant;
import com.example.payment.data.model.merchant.MerchantStatus;
import com.example.payment.data.repo.MerchantRepository;
import com.example.payment.error.exception.MerchantNonDeletableException;
import com.example.payment.error.exception.MerchantNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MerchantServiceTest {

    private static final String TEST_MERCHANT_NAME = "TEST MERCHANT";
    private static final Merchant TEST_MERCHANT = new Merchant();

    private static final String TEST_UPDATE_VALUE = "TEST UP";
    private static final MerchantUpdate TEST_MERCHANT_UPDATE = new MerchantUpdate(TEST_UPDATE_VALUE, TEST_UPDATE_VALUE,
            TEST_UPDATE_VALUE, MerchantStatus.INACTIVE);

    static {
        TEST_MERCHANT.setName(TEST_MERCHANT_NAME);
    }

    private static final MerchantInfo TEST_MERCHANT_INFO = new MerchantInfo(TEST_MERCHANT_NAME, null, null, null, null);
    private static final List<Merchant> TEST_MERCHANTS = List.of(TEST_MERCHANT, TEST_MERCHANT, TEST_MERCHANT);

    @Mock
    private Merchant merchantToUpdate;

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private MerchantMapper merchantMapper;

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private MerchantService merchantService;

    @Test
    void testGetMerchants() {
        when(merchantRepository.findAll()).thenReturn(TEST_MERCHANTS);
        when(merchantMapper.toInfo(TEST_MERCHANT)).thenReturn(TEST_MERCHANT_INFO);

        List<MerchantInfo> result = merchantService.getMerchants();

        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isEqualTo(false);
        assertThat(result.size()).isEqualTo(TEST_MERCHANTS.size());

        for (MerchantInfo info : result) {
            assertThat(info).isEqualTo(TEST_MERCHANT_INFO);
        }

        verify(merchantRepository, times(1)).findAll();
        verify(merchantMapper, times(TEST_MERCHANTS.size())).toInfo(TEST_MERCHANT);
    }

    @Test
    void testGetMerchant() throws MerchantNotFoundException {
        when(merchantRepository.findByName(TEST_MERCHANT_NAME)).thenReturn(Optional.of(TEST_MERCHANT));
        when(merchantMapper.toInfo(TEST_MERCHANT)).thenReturn(TEST_MERCHANT_INFO);

        MerchantInfo result = merchantService.getMerchant(TEST_MERCHANT_NAME);
        assertThat(result).isEqualTo(TEST_MERCHANT_INFO);

        verify(merchantRepository, times(1)).findByName(TEST_MERCHANT_NAME);
        verify(merchantMapper, times(1)).toInfo(TEST_MERCHANT);
    }

    @Test
    void testGetMerchantNotFound() {
        when(merchantRepository.findByName(TEST_MERCHANT_NAME)).thenReturn(Optional.empty());

        assertThrows(MerchantNotFoundException.class, () -> merchantService.getMerchant(TEST_MERCHANT_NAME));
    }

    @Test
    void testUpdateMerchant() throws MerchantNotFoundException {
        when(merchantRepository.findByName(TEST_MERCHANT_NAME)).thenReturn(Optional.of(merchantToUpdate));

        merchantService.updateMerchant(TEST_MERCHANT_NAME, TEST_MERCHANT_UPDATE);

        verify(merchantToUpdate, times(1)).setName(TEST_UPDATE_VALUE);
        verify(merchantToUpdate, times(1)).setDescription(TEST_UPDATE_VALUE);
        verify(merchantToUpdate, times(1)).setEmail(TEST_UPDATE_VALUE);
        verify(merchantToUpdate, times(1)).setStatus(MerchantStatus.INACTIVE);
        verify(merchantRepository, times(1)).save(merchantToUpdate);
    }

    @Test
    void testUpdateMerchantNotFound() {
        when(merchantRepository.findByName(TEST_MERCHANT_NAME)).thenReturn(Optional.empty());

        assertThrows(MerchantNotFoundException.class, () -> merchantService.updateMerchant(
                TEST_MERCHANT_NAME, TEST_MERCHANT_UPDATE));
    }

    @Test
    void testDeleteMerchantWithAccount() throws MerchantNotFoundException, MerchantNonDeletableException {
        when(merchantRepository.findByName(TEST_MERCHANT_NAME)).thenReturn(Optional.of(TEST_MERCHANT));

        merchantService.deleteMerchant(TEST_MERCHANT_NAME);

        verify(transactionService, times(1)).merchantHasTransactions(TEST_MERCHANT);
        verify(accountService, times(1)).removeMerchantFromAccount(TEST_MERCHANT);
        verify(merchantRepository, times(1)).delete(TEST_MERCHANT);
    }

    @Test
    void testDeleteMerchantNotFound() {
        when(merchantRepository.findByName(TEST_MERCHANT_NAME)).thenReturn(Optional.empty());

        assertThrows(MerchantNotFoundException.class, () -> merchantService.deleteMerchant(TEST_MERCHANT_NAME));
    }

    @Test
    void testDeleteMerchantNonDeletable() {
        when(merchantRepository.findByName(TEST_MERCHANT_NAME)).thenReturn(Optional.of(TEST_MERCHANT));
        when(transactionService.merchantHasTransactions(TEST_MERCHANT)).thenReturn(true);

        assertThrows(MerchantNonDeletableException.class, () -> merchantService.deleteMerchant(TEST_MERCHANT_NAME));
    }
}
