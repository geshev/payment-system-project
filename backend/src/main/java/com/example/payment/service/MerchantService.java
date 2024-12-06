package com.example.payment.service;

import com.example.payment.data.dto.merchant.MerchantCreation;
import com.example.payment.data.dto.merchant.MerchantInfo;
import com.example.payment.data.mapper.MerchantMapper;
import com.example.payment.data.model.Merchant;
import com.example.payment.data.repo.MerchantRepository;
import com.example.payment.util.CSVUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;
    private final AccountService accountService;

    public MerchantService(MerchantRepository merchantRepository, MerchantMapper merchantMapper, AccountService accountService) throws IOException {
        this.merchantRepository = merchantRepository;
        this.merchantMapper = merchantMapper;
        this.accountService = accountService;
        loadMerchants();
    }

    private void loadMerchants() throws IOException {
        List<MerchantCreation> merchants = CSVUtils.loadCSV("data/merchants.csv", MerchantCreation.class);
        merchants.forEach(merchantCreation -> {
            if (!merchantRepository.existsByNameAndEmail(merchantCreation.getName(), merchantCreation.getEmail())) {
                Merchant merchant = merchantMapper.fromMerchantCreation(merchantCreation);
                merchantRepository.save(merchant);
                if (accountService.accountExists(merchantCreation.getUsername())) {
                    accountService.addMerchantToAccount(merchantCreation.getUsername(), merchant);
                }
            }
        });
    }

    public List<MerchantInfo> getMerchants() {
        return merchantRepository.findAll().stream().map(merchantMapper::toInfo).toList();
    }
}
