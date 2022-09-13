package com.paymybuddy;

import com.paymybuddy.dal.entity.*;
import com.paymybuddy.dal.repository.LogRepository;
import com.paymybuddy.dal.repository.MovementRepository;
import com.paymybuddy.dal.repository.TransactionRepository;
import com.paymybuddy.dal.repository.UserRepository;
import com.paymybuddy.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ActiveProfiles("test") // Utilisation du application-test.properties pour les tests à la place du application.properties (BDD de test)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //Me permet de faire mon Before all
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
    private static final String emailKo = "emailKO@test.com";
    private static final Double amountMovement = 10.0;
    private static final Double amountTransaction = 20.0;
    private static final Double transactionCommission = 1.0;

    private User user1;
    private User user2;

    @BeforeEach
    public void initDataBase(){
        //Reinit de la base
        transactionRepository.deleteAll();
        movementRepository.deleteAll();
        logRepository.deleteAll();
        userRepository.deleteAll();

        //User 1 - log et user
        Log log1 = new Log();
        log1.setEmail(email1);
        log1.setMdp(mdp1);
        user1 = new User();
        user1.setFirstName(firstName1);
        user1.setLastName(lastName1);
        user1.setBalance(balance1);
        user1.getMovements().clear();
        user1.getTransactions().clear();
        user1.getConnections().clear();
        user1 = userRepository.save(user1);
        log1.setUser(user1);
        logRepository.hashPasswordAndSave(log1);

        //User 2 - log et user
        Log log2 = new Log();
        user2 = new User();
        user2.setFirstName(firstName2);
        user2.setLastName(lastName2);
        user2.setBalance(balance2);
        user2.getMovements().clear();
        user2.getTransactions().clear();
        user2.getConnections().clear();
        user2 = userRepository.save(user2);
        log2.setEmail(email2);
        log2.setMdp(mdp2);
        log2.setUser(user2);
        logRepository.hashPasswordAndSave(log2);
    }

    @AfterAll
    public void emptyBase() {
        transactionRepository.deleteAll();
        movementRepository.deleteAll();
        logRepository.deleteAll();
        userRepository.deleteAll();
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
        Double oldbalance = user1.getBalance();
        User userTest = userService.addMovementToUser(user1, movement);
        assertEquals(oldbalance+amountMovement, userTest.getBalance());
        assertEquals(1, userTest.getMovements().size());
    }

    @Test
    @Transactional
    public void addMovementDebitToUserTest() {
        Movement movement = new Movement();
        movement.setType(TypeMovement.DEBIT);
        movement.setAmount(amountMovement);
        Double oldbalance = user1.getBalance();
        User userTest = userService.addMovementToUser(user1, movement);
        assertEquals(oldbalance-amountMovement, userTest.getBalance());
        assertEquals(1, userTest.getMovements().size());
    }

    @Test
    @Transactional
    public void addTransactionToUserTest() {
        Transaction transaction = new Transaction();
        transaction.setAmount(amountTransaction);
        transaction.setCommission(transactionCommission);
        transaction.setUserFrom(user1);
        transaction.setUserTo(user2);
        Double oldbalance1 = user1.getBalance();
        Double oldBalance2 = user2.getBalance();
        User userTest = userService.addTransactionToUser(user1, user2, transaction);
        assertEquals(oldbalance1-amountTransaction, userTest.getBalance());
        assertEquals(oldBalance2+amountTransaction-transactionCommission, user2.getBalance());
        assertEquals(1, userTest.getTransactions().size());
    }
}
