package com.example.payment.data.repo;

import com.example.payment.data.model.merchant.Merchant;
import com.example.payment.data.model.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByMerchant(Merchant merchant);
}
