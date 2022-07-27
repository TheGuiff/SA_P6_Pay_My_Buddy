package com.paymybuddy.service;

import com.paymybuddy.dal.entity.Transaction;
import com.paymybuddy.dal.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Iterable<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById (Long id) {
        return transactionRepository.findById(id);
    }

    public Transaction addTransaction (Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public void deleteTransactionById (Long id) {
        transactionRepository.deleteById(id);
    }

}
