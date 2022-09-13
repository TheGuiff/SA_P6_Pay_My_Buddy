package com.paymybuddy.service;

import com.paymybuddy.dal.entity.Transaction;
import com.paymybuddy.dal.repository.TransactionRepository;
import com.paymybuddy.web.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public Transaction newTransactionService (TransactionDto transactionDto) throws Exception {
        // Transaction de montant non nul et de solde suffisant
        // Attention - ne crée que la transaction - on fait ensuite un addTransactionToUser de UserService (voir TransactionTestIT
        if (transactionDto.getAmount() > 0.0) {
            if ( transactionDto.getAmount() > transactionDto.getUserFrom().getBalance()) {
                throw new Exception("Account balance (" + transactionDto.getUserFrom().getBalance() + ") insufficient for debit (" + transactionDto.getAmount() + ")");
            } if (transactionDto.getUserFrom().getConnections().contains(transactionDto.getUserTo())) {
                Transaction transaction = new Transaction();
                transaction.setDescription(transactionDto.getDescription());
                transaction.setDateTransaction(LocalDateTime.now());
                transaction.setAmount(transactionDto.getAmount());
                transaction.setCommission(Math.ceil(transactionDto.getAmount()*0.5)/100);
                transaction.setUserFrom(transactionDto.getUserFrom());
                transaction.setUserTo(transactionDto.getUserTo());
                return transactionRepository.save(transaction);
            }
            // Si le user de destination n'est pas dans les contacts du user d'origine
            else {
                throw new Exception("User " + transactionDto.getUserTo().getFirstName() + ", " + transactionDto.getUserTo().getLastName() +
                        " is not a connection of " + transactionDto.getUserFrom().getFirstName() + ", " + transactionDto.getUserFrom().getLastName());
            }
        } else {
            throw new Exception("Transaction with no account");
        }
    }

}
