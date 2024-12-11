package com.example.payment.data.repo;

import com.example.payment.data.model.merchant.Merchant;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    Optional<Merchant> findByName(String name);

    boolean existsByNameAndEmail(String name, String email);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT m FROM Merchant m WHERE m.id = :id")
    Merchant findMerchantForTotalSumUpdate(@Param("id") Long id);
}
