package com.paymybuddy;

import com.paymybuddy.dal.entity.*;
import com.paymybuddy.dal.repository.LogRepository;
import com.paymybuddy.dal.repository.MovementRepository;
import com.paymybuddy.dal.repository.TransactionRepository;
import com.paymybuddy.dal.repository.UserRepository;
import com.paymybuddy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@TestPropertySource(locations="/application-test.properties")
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

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
    private static final String email3 = "email3@test.com";
    private static final String mdp3 = "mdpTest3";
    private static final String firstName3 = "Firstname3";
    private static final String lastName3 = "Lastname3";
    private static final Double balance3 = 150.0;
    private static final String email4 = "email4@test.com";
    private static final String mdp4 = "mdpTest4";
    private static final String firstName4 = "Firstname4";
    private static final String lastName4 = "Lastname4";
    private static final Double balance4 = 90.0;
    private static final String email5 = "email5@test.com";
    private static final String mdp5 = "mdpTest5";
    private static final String firstName5 = "Firstname5";
    private static final String lastName5 = "Lastname5";
    private static final Double balance5 = 75.0;
    private static final String emailKo = "emailKO@test.com";
    private static final Double amountMovement = 10.0;
    private static final Double amountTransaction = 20.0;
    private static final Double transactionCommission = 1.0;

    private User user1 = new User();
    private User user2 = new User();
    private User user3 = new User();
    private User user4 = new User();
    private User user5 = new User();
    private Log log1 = new Log();
    private Log log2 = new Log();
    private Log log3 = new Log();
    private Log log4 = new Log();
    private Log log5 = new Log();

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
        log1 = logRepository.hashPasswordAndSave(log1);

        //User 2 - log et user
        user2.setFirstName(firstName2);
        user2.setLastName(lastName2);
        user2.setBalance(balance2);
        user2 = userRepository.save(user2);
        log2.setEmail(email2);
        log2.setMdp(mdp2);
        log2.setUser(user2);
        log2 = logRepository.hashPasswordAndSave(log2);

        //User 3 - log et user
        user3.setFirstName(firstName3);
        user3.setLastName(lastName3);
        user3.setBalance(balance3);
        user3 = userRepository.save(user3);
        log3.setEmail(email3);
        log3.setMdp(mdp3);
        log3.setUser(user3);
        log3 = logRepository.hashPasswordAndSave(log3);

        //User 4 - log et user
        user4.setFirstName(firstName4);
        user4.setLastName(lastName4);
        user4.setBalance(balance4);
        user4 = userRepository.save(user4);
        log4.setEmail(email4);
        log4.setMdp(mdp4);
        log4.setUser(user4);
        log4 = logRepository.hashPasswordAndSave(log4);

        //User 5 - log et user
        user5.setFirstName(firstName5);
        user5.setLastName(lastName5);
        user5.setBalance(balance5);
        user5 = userRepository.save(user5);
        log5.setEmail(email5);
        log5.setMdp(mdp5);
        log5.setUser(user5);
        log5 = logRepository.hashPasswordAndSave(log5);
    }

    @Test
    public void addConnectionTestOk () {
        user1 = userService.addConnection(user1, email2);
        assertEquals(1, user1.getConnections().size());
    }

    @Test
    public void addConnectionTestKo () {
        Assertions.assertThrows(NoSuchElementException.class,() -> userService.addConnection(user1, emailKo));
    }

    @Test
    @Transactional
    public void addMovementCreditToUserTest() {
        Movement movement = new Movement();
        movement.setType(TypeMovement.CREDIT);
        movement.setAmount(amountMovement);
        Double oldbalance = user3.getBalance();
        User userTest = userService.addMovementToUser(user3, movement);
        assertEquals(oldbalance+amountMovement, userTest.getBalance());
        assertEquals(1, userTest.getMovements().size());
    }

    @Test
    @Transactional
    public void addMovementDebitToUserTest() {
        Movement movement = new Movement();
        movement.setType(TypeMovement.DEBIT);
        movement.setAmount(amountMovement);
        Double oldbalance = user4.getBalance();
        User userTest = userService.addMovementToUser(user4, movement);
        assertEquals(oldbalance-amountMovement, userTest.getBalance());
        assertEquals(1, userTest.getMovements().size());
    }

    @Test
    @Transactional
    public void addTransactionToUserTest() {
        Transaction transaction = new Transaction();
        transaction.setAmount(amountTransaction);
        transaction.setCommission(transactionCommission);
        transaction.setUserFrom(user2);
        transaction.setUserTo(user5);
        Double oldbalance2 = user2.getBalance();
        Double oldBalance5 = user5.getBalance();
        User userTest = userService.addTransactionToUser(user2, user5, transaction);
        assertEquals(oldbalance2-amountTransaction, userTest.getBalance());
        assertEquals(oldBalance5+amountTransaction-transactionCommission, user5.getBalance());
        assertEquals(1, userTest.getTransactions().size());
    }
}
