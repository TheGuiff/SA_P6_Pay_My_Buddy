package com.paymybuddy.integration;

import com.paymybuddy.dal.entity.*;
import com.paymybuddy.dal.repository.LogRepository;
import com.paymybuddy.dal.repository.MovementRepository;
import com.paymybuddy.dal.repository.TransactionRepository;
import com.paymybuddy.dal.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest
@TestPropertySource(locations="/application-test.properties")
public class DataBaseTestsIT {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MovementRepository movementRepository;

    @Test
    public void tests_init () {
        //Reinit de la base
        transactionRepository.deleteAll();
        movementRepository.deleteAll();
        logRepository.deleteAll();
        userRepository.deleteAll();

        //User 1 - log et user
        Log newLog = new Log();
        newLog.setEmail("email@test.com");
        newLog.setMdp("mdpTest");
        User newUser = new User();
        newUser.setFirstName("FirstName");
        newUser.setLastName("LastName");
        newUser = userRepository.save(newUser);
        newLog.setUser(newUser);
        newLog = logRepository.save(newLog);

        //Transaction sur le user 1
        Transaction newTransaction = new Transaction();
        newTransaction.setDescription("Transaction de test");
        newTransaction.setUserFrom(newUser);
        newTransaction = transactionRepository.save(newTransaction);

        //Mouvement sur le user 1
        Movement newMovement = new Movement();
        newMovement.setAmount(200.0);
        newMovement.setType(TypeMovement.CREDIT);
        newMovement.setUser(newUser);
        newMovement = movementRepository.save(newMovement);

        //User 2 - log et user
        User newUser2 = new User();
        newUser2.setFirstName("FirstName2");
        newUser2.setLastName("LastName2");
        newUser2 = userRepository.save(newUser2);
        Log newLog2 = new Log();
        newLog2.setEmail("email2@test.com");
        newLog2.setMdp("mdpTest2");
        newLog2.setUser(newUser2);

        //User 2 dans la liste de contacts de User 1
        newLog2 = logRepository.save(newLog2);
        newUser.getConnections().add(newUser2);
        newUser = userRepository.save(newUser);
    }
}
