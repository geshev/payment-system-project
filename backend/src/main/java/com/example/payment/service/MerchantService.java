package com.example.payment.service;

import com.example.payment.data.dto.merchant.MerchantCreation;
import com.example.payment.data.dto.merchant.MerchantInfo;
import com.example.payment.data.dto.merchant.MerchantUpdate;
import com.example.payment.data.mapper.MerchantMapper;
import com.example.payment.data.model.merchant.Merchant;
import com.example.payment.data.repo.MerchantRepository;
import com.example.payment.error.exception.MerchantNonDeletableException;
import com.example.payment.error.exception.MerchantNotFoundException;
import com.example.payment.util.CSVUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;
    private final AccountService accountService;
    private final TransactionService transactionService;

    public MerchantService(final MerchantRepository merchantRepository, final MerchantMapper merchantMapper,
                           final AccountService accountService, @Lazy final TransactionService transactionService)
            throws IOException {
        this.merchantRepository = merchantRepository;
        this.merchantMapper = merchantMapper;
        this.accountService = accountService;
        this.transactionService = transactionService;
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

    public MerchantInfo getMerchant(final String name) throws MerchantNotFoundException {
        return merchantMapper.toInfo(merchantRepository.findByName(name).orElseThrow(MerchantNotFoundException::new));
    }

    public void updateMerchant(final String name, final MerchantUpdate update) throws MerchantNotFoundException {
        Merchant merchant = merchantRepository.findByName(name).orElseThrow(MerchantNotFoundException::new);

        merchant.setName(update.name());
        merchant.setDescription(update.description());
        merchant.setEmail(update.email());
        merchant.setStatus(update.status());

        merchantRepository.save(merchant);
    }

    public void deleteMerchant(final String name) throws MerchantNotFoundException, MerchantNonDeletableException {
        Merchant merchant = merchantRepository.findByName(name).orElseThrow(MerchantNotFoundException::new);
        if (transactionService.merchantHasTransactions(merchant)) {
            throw new MerchantNonDeletableException();
        } else {
            accountService.removeMerchantFromAccount(merchant);
            merchantRepository.delete(merchant);
        }
    }

    void updateMerchantTotalSum(final Merchant merchant, final BigDecimal update) {
        Merchant updatedMerchant = merchantRepository.findMerchantForTotalSumUpdate(merchant.getId());
        updatedMerchant.setTotalTransactionSum(updatedMerchant.getTotalTransactionSum().add(update));
        merchantRepository.save(updatedMerchant);
    }
}
