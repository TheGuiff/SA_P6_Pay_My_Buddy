package com.paymybuddy.dal.repository;

import com.paymybuddy.dal.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
