package com.paymybuddy.service;

import com.paymybuddy.dal.entity.Transaction;
import com.paymybuddy.web.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

@Service
public class TransactionInternalService {

    @Autowired
    TransactionService transactionService;

    @Autowired
    UserService userService;

    @Required
    public Transaction newTransactionInternalService (TransactionDto transactionDto) throws Exception {
        Transaction transaction = transactionService.newTransactionService(transactionDto);
        userService.addTransactionToUser(transactionDto.getUserFrom(), transactionDto.getUserTo(), transaction);
        return transaction;
    }
}
