package com.example.payment.controller;

import com.example.payment.data.dto.merchant.MerchantInfo;
import com.example.payment.data.dto.merchant.MerchantUpdate;
import com.example.payment.error.exception.MerchantNonDeletableException;
import com.example.payment.error.exception.MerchantNotFoundException;
import com.example.payment.service.MerchantService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("merchants")
public class MerchantController {

    private final MerchantService merchantService;

    public MerchantController(final MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @GetMapping
    public List<MerchantInfo> getMerchants() {
        return merchantService.getMerchants();
    }

    @GetMapping("{name}")
    public MerchantInfo getMerchant(@PathVariable final String name) throws MerchantNotFoundException {
        return merchantService.getMerchant(name);
    }

    @PutMapping("{name}")
    public void updateMerchant(@PathVariable final String name,
                               @RequestBody @Valid final MerchantUpdate update) throws MerchantNotFoundException {
        merchantService.updateMerchant(name, update);
    }

    @DeleteMapping("{name}")
    public void deleteMerchant(@PathVariable final String name)
            throws MerchantNotFoundException, MerchantNonDeletableException {
        merchantService.deleteMerchant(name);
    }
}
