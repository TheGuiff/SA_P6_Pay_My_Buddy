package com.paymybuddy.service;

import com.paymybuddy.dal.entity.Movement;
import com.paymybuddy.dal.entity.Transaction;
import com.paymybuddy.dal.entity.TypeMovement;
import com.paymybuddy.dal.repository.TransactionRepository;
import com.paymybuddy.web.dto.MovementDto;
import com.paymybuddy.web.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
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

    @Transactional
    public Transaction newTransactionService (TransactionDto transactionDto) throws Exception {
        if (transactionDto.getAmount() > 0.0) {
            if ( transactionDto.getAmount() > transactionDto.getUserFrom().getBalance()) {
                throw new Exception("Account balance (" + transactionDto.getUserFrom().getBalance() + ") insufficient for debit (" + transactionDto.getAmount() + ")");
            } else {
                Transaction transaction = new Transaction();
                transaction.setDescription(transactionDto.getDescription());
                transaction.setDateTransaction(LocalDateTime.now());
                transaction.setAmount(transactionDto.getAmount());
                transaction.setCommission(Math.ceil(transactionDto.getAmount()*0.5)/100);
                transaction.setUserFrom(transactionDto.getUserFrom());
                transaction.setUserTo(transactionDto.getUserTo());
                return transactionRepository.save(transaction);
            }
        } else {
            throw new Exception("Transaction with no account");
        }
    }

}
