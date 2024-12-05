package com.example.payment.data.repo;

import com.example.payment.data.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    boolean existsByNameAndEmail(String name, String email);
}
