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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations="/application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MovementTestTI {

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

    private static String email1 = "email1@test.com";
    private static String mdp1 = "mdpTest1";
    private static String firstName1 = "Firstname1";
    private static String lastName1 = "Lastname1";
    private static Double balance1 = 100.0;
    private static String email2 = "email2@test.com";
    private static String mdp2 = "mdpTest2";
    private static String firstName2 = "Firstname2";
    private static String lastName2 = "Lastname2";
    private static Double balance2 = 80.0;
    private static Double amountMovement = 10.0;

    private User user1 = new User();
    private User user2 = new User();

    private Log log1 = new Log();
    private Log log2 = new Log();

    private MovementDto movementDto = new MovementDto();

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
    public void movementCreditTIOk() throws Exception {
        int nbMovements = user1.getMovements().size();
        movementDto.setType(TypeMovement.CREDIT);
        movementDto.setAmount(amountMovement);
        movementDto.setUser(user1);
        Movement movement = movementService.newMovementService(movementDto);
        user1 = userService.addMovementToUser(user1, movement);
        assertEquals((balance1+amountMovement), user1.getBalance());
    }

    @Test
    public void movementDebitTIOk() throws Exception {
        int nbMovements = user2.getMovements().size();
        movementDto.setType(TypeMovement.DEBIT);
        movementDto.setAmount(amountMovement);
        movementDto.setUser(user2);
        Movement movement = movementService.newMovementService(movementDto);
        user2 = userService.addMovementToUser(user2, movement);
        assertEquals((balance2-amountMovement), user2.getBalance());
    }
}
