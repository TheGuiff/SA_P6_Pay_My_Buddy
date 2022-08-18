package com.paymybuddy;

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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations="/application-test.properties")
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MovementServiceTest {

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
    private static final Double amountMovement = 10.0;

    private User user1 = new User();
    private User user2 = new User();
    private User user3 = new User();
    private User user4 = new User();
    private Log log1 = new Log();
    private Log log2 = new Log();
    private Log log3 = new Log();
    private Log log4 = new Log();
    private final MovementDto movementDto = new MovementDto();

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
    }

    @Test
    public void movementNullWhenNoAmount() {
        movementDto.setType(TypeMovement.CREDIT);
        movementDto.setAmount(0.0);
        movementDto.setUser(user4);
        assertThrows(Exception.class,() -> movementService.newMovementService(movementDto));
    }

    @Test
    public void movementKONotEnoughBalance() {
        movementDto.setType(TypeMovement.DEBIT);
        movementDto.setAmount(amountMovement+balance3);
        movementDto.setUser(user3);
        Assertions.assertThrows(Exception.class,() -> movementService.newMovementService(movementDto));
    }

    @Test
    public void movementCreditOk() throws Exception {
        movementDto.setType(TypeMovement.CREDIT);
        movementDto.setAmount(amountMovement);
        movementDto.setUser(user1);
        Movement movement = movementService.newMovementService(movementDto);
        assertTrue(movementRepository.findById(movement.getId()).isPresent());
    }

    @Test
    public void movementDebitOk() throws Exception {
        movementDto.setType(TypeMovement.DEBIT);
        movementDto.setAmount(amountMovement);
        movementDto.setUser(user2);
        Movement movement = movementService.newMovementService(movementDto);
        assertTrue(movementRepository.findById(movement.getId()).isPresent());
    }
}
