package com.paymybuddy;

import com.paymybuddy.dal.entity.*;
import com.paymybuddy.dal.repository.LogRepository;
import com.paymybuddy.dal.repository.MovementRepository;
import com.paymybuddy.dal.repository.TransactionRepository;
import com.paymybuddy.dal.repository.UserRepository;
import com.paymybuddy.service.MovementService;
import com.paymybuddy.service.UserService;
import com.paymybuddy.web.dto.LogDto;
import com.paymybuddy.web.dto.MovementDto;
import org.apache.commons.lang.text.StrBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


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
    private static String email3 = "email3@test.com";
    private static String mdp3 = "mdpTest3";
    private static String firstName3 = "Firstname3";
    private static String lastName3 = "Lastname3";
    private static Double balance3 = 150.0;
    private static String email4 = "email4@test.com";
    private static String mdp4 = "mdpTest4";
    private static String firstName4 = "Firstname4";
    private static String lastName4 = "Lastname4";
    private static Double balance4 = 90.0;
    private static String emailKo = "emailKO@test.com";
    private static String mdpKo = "mdpKoTest";
    private static Double amountMovement = 10.0;

    private User user1 = new User();
    private User user2 = new User();
    private User user3 = new User();
    private User user4 = new User();
    private Log log1 = new Log();
    private Log log2 = new Log();
    private Log log3 = new Log();
    private Log log4 = new Log();
    private LogDto logDto = new LogDto();
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

        //User 3 - log et user
        user3.setFirstName(firstName3);
        user3.setLastName(lastName3);
        user3.setBalance(balance3);
        user3 = userRepository.save(user3);
        log3.setEmail(email3);
        log3.setMdp(mdp3);
        log3.setUser(user3);
        log3 = logRepository.save(log3);

        //User 4 - log et user
        user4.setFirstName(firstName4);
        user4.setLastName(lastName4);
        user4.setBalance(balance4);
        user4 = userRepository.save(user4);
        log4.setEmail(email4);
        log4.setMdp(mdp4);
        log4.setUser(user4);
        log4 = logRepository.save(log4);
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
    public void logInTestOk() {
        logDto.setEmail(email1);
        logDto.setMdp(mdp1);
        Log logOut = userService.logAndGetUser(logDto);
        assertEquals(email1, logOut.getEmail());
        assertEquals(mdp1, logOut.getMdp());
        assertEquals(firstName1, logOut.getUser().getFirstName());
        assertEquals(lastName1, logOut.getUser().getLastName());
    }

    @Test
    public void logInKoUnknownUser() {
        logDto.setEmail(emailKo);
        logDto.setMdp(mdp1);
        Assertions.assertThrows(NoSuchElementException.class,() -> userService.logAndGetUser(logDto));
    }

    @Test
    public void logInKoWrongPassword() {
        logDto.setEmail(email1);
        logDto.setMdp(mdpKo);
        Assertions.assertThrows(NoSuchElementException.class,() -> userService.logAndGetUser(logDto));
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
}
