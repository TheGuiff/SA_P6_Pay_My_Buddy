package com.paymybuddy.integration;

import com.paymybuddy.dal.entity.*;
import com.paymybuddy.dal.repository.LogRepository;
import com.paymybuddy.dal.repository.MovementRepository;
import com.paymybuddy.dal.repository.TransactionRepository;
import com.paymybuddy.dal.repository.UserRepository;
import com.paymybuddy.service.TransactionService;
import com.paymybuddy.service.UserService;
import com.paymybuddy.web.dto.TransactionDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations="/application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionTestIT {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LogRepository logRepository;

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    MovementRepository movementRepository;
    @Autowired
    UserService userService;
    @Autowired
    TransactionService transactionService;

    private static final String email1 = "email1@test.com";
    private static final String mdp1 = "mdpTest1";
    private static final String firstName1 = "Firstname1";
    private static final String lastName1 = "Lastname1";
    private static final Double balance1 = 100.0;
    private static final String email2 = "email2@test.com";
    private static final String mdp2 = "mdpTest2";
    private static final String firstName2 = "Firstname2";
    private static final String lastName2 = "Lastname2";
    private static final Double balance2 = 80.0;
    private static final Double amountTransaction = 50.0;

    private User user1 = new User();
    private User user2 = new User();

    private Log log1 = new Log();
    private Log log2 = new Log();

    private final TransactionDto transactionDto = new TransactionDto();

    @BeforeAll
    public void initDataBase(){
        //Reinit de la base
        transactionRepository.deleteAll();
        movementRepository.deleteAll();
        logRepository.deleteAll();
        userRepository.deleteAll();

        //User 1 - log et user
        log1.setEmail(email1);
        log1.setMdp(mdp1);
        user1.setFirstName(firstName1);
        user1.setLastName(lastName1);
        user1.setBalance(balance1);
        user1 = userRepository.save(user1);
        log1.setUser(user1);
        log1 = logRepository.save(log1);

        //User 2 - log et user
        user2.setFirstName(firstName2);
        user2.setLastName(lastName2);
        user2.setBalance(balance2);
        user2 = userRepository.save(user2);
        log2.setEmail(email2);
        log2.setMdp(mdp2);
        log2.setUser(user2);
        log2 = logRepository.save(log2);
    }

    @Test
    public void transactionTestIt() throws Exception {
        transactionDto.setDescription("Test transaction");
        transactionDto.setAmount(amountTransaction);
        transactionDto.setUserFrom(user1);
        transactionDto.setUserTo(user2);
        Transaction transaction = transactionService.newTransactionService(transactionDto);
        user1 = userService.addTransactionToUser(user1, user2, transaction);
        assertEquals((balance1-amountTransaction), user1.getBalance());
        assertEquals(balance2+amountTransaction-transaction.getCommission(),user2.getBalance());
    }
}
