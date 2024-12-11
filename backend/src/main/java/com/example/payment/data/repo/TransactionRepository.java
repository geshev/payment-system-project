package com.example.payment.data.repo;

import com.example.payment.data.model.merchant.Merchant;
import com.example.payment.data.model.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByMerchant(Merchant merchant);

    boolean existsByMerchant(Merchant merchant);

    boolean existsByMerchantAndUuid(Merchant merchant, UUID uuid);

    void deleteAllByCreatedBefore(LocalDateTime threshold);

    @Query("SELECT t from Transaction t WHERE TYPE(t) = :type"
            + " and t.merchant = :merchant and t.referenceId = :referenceId and t.status = 'APPROVED'")
    Optional<Transaction> findReferencedTransaction(@Param("type") Class<? extends Transaction> type,
                                                    @Param("merchant") Merchant merchant,
                                                    @Param("referenceId") String referenceId);
}
