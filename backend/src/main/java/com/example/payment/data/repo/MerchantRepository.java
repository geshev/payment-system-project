package com.example.payment.data.repo;

import com.example.payment.data.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    Optional<Merchant> findByName(String name);

    boolean existsByNameAndEmail(String name, String email);
}
