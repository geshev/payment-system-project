package com.example.payment.unit.merchant;

import com.example.payment.data.dto.merchant.MerchantInfo;
import com.example.payment.data.mapper.MerchantMapper;
import com.example.payment.data.model.Merchant;
import com.example.payment.data.repo.MerchantRepository;
import com.example.payment.error.exception.MerchantNotFoundException;
import com.example.payment.service.AccountService;
import com.example.payment.service.MerchantService;
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

    private static final Long TEST_MERCHANT_ID = 1L;
    private static final String TEST_MERCHANT_NAME = "TEST MERCHANT";
    private static final Merchant TEST_MERCHANT = new Merchant();

    static {
        TEST_MERCHANT.setId(TEST_MERCHANT_ID);
        TEST_MERCHANT.setName(TEST_MERCHANT_NAME);
    }

    private static final MerchantInfo TEST_MERCHANT_INFO = new MerchantInfo(TEST_MERCHANT_NAME, null, null, null, null);
    private static final List<Merchant> TEST_MERCHANTS = List.of(TEST_MERCHANT, TEST_MERCHANT, TEST_MERCHANT);

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private MerchantMapper merchantMapper;

    @Mock
    private AccountService accountService;

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
        when(merchantRepository.findById(TEST_MERCHANT_ID)).thenReturn(Optional.of(TEST_MERCHANT));
        when(merchantMapper.toInfo(TEST_MERCHANT)).thenReturn(TEST_MERCHANT_INFO);

        MerchantInfo result = merchantService.getMerchant(TEST_MERCHANT_ID);
        assertThat(result).isEqualTo(TEST_MERCHANT_INFO);

        verify(merchantRepository, times(1)).findById(TEST_MERCHANT_ID);
        verify(merchantMapper, times(1)).toInfo(TEST_MERCHANT);
    }

    @Test
    void testGetMerchantNotFound() {
        when(merchantRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(MerchantNotFoundException.class, () -> merchantService.getMerchant(TEST_MERCHANT_ID));
    }
}
