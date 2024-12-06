package com.example.payment.unit.merchant;

import com.example.payment.data.dto.merchant.MerchantInfo;
import com.example.payment.data.mapper.MerchantMapper;
import com.example.payment.data.model.Merchant;
import com.example.payment.data.repo.MerchantRepository;
import com.example.payment.service.AccountService;
import com.example.payment.service.MerchantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MerchantServiceTest {

    private static final String TEST_MERCHANT_NAME = "TEST MERCHANT";
    private static final Merchant TEST_MERCHANT = new Merchant();
    static {
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
    public void testGetMerchants() {
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
}
