package com.paymybuddy.integration;

import com.paymybuddy.dal.entity.Log;
import com.paymybuddy.dal.entity.Movement;
import com.paymybuddy.dal.entity.TypeMovement;
import com.paymybuddy.dal.entity.User;
import com.paymybuddy.dal.repository.LogRepository;
import com.paymybuddy.dal.repository.MovementRepository;
import com.paymybuddy.dal.repository.TransactionRepository;
import com.paymybuddy.dal.repository.UserRepository;
import com.paymybuddy.service.MovementService;
import com.paymybuddy.service.UserService;
import com.paymybuddy.web.dto.MovementDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test") // Utilisation du application-test.properties pour les tests à la place du application.properties (BDD de test)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //Me permet de faire mon @AfterAll
public class MovementTestIT {

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
    MovementService movementService;

    private static final String email1 = "email1@test.com";
    private static final String mdp1 = "mdpTest1";
    private static final String firstName1 = "Firstname1";
    private static final String lastName1 = "Lastname1";
    private static final Double balance = 100.0;
    private static final Double amountMovement = 10.0;

    private User user1;

    private final MovementDto movementDto = new MovementDto();

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
        user1.setBalance(balance);
        user1 = userRepository.save(user1);
        log1.setUser(user1);
        logRepository.hashPasswordAndSave(log1);

    }

    @AfterAll
    public void emptyBase() {
        transactionRepository.deleteAll();
        movementRepository.deleteAll();
        logRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void movementCreditTIOk() throws Exception {
        movementDto.setType(TypeMovement.CREDIT);
        movementDto.setAmount(amountMovement);
        movementDto.setUser(user1);
        Movement movement = movementService.newMovementService(movementDto);
        user1 = userService.addMovementToUser(user1, movement);
        assertEquals((balance+amountMovement), user1.getBalance());
    }

    @Test
    public void movementDebitTIOk() throws Exception {
        movementDto.setType(TypeMovement.DEBIT);
        movementDto.setAmount(amountMovement);
        movementDto.setUser(user1);
        Movement movement = movementService.newMovementService(movementDto);
        user1 = userService.addMovementToUser(user1, movement);
        assertEquals((balance-amountMovement), user1.getBalance());
    }
}
