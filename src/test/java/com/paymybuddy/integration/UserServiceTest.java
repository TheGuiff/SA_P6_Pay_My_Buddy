package com.paymybuddy.integration;

import com.paymybuddy.dal.entity.*;
import com.paymybuddy.dal.repository.LogRepository;
import com.paymybuddy.dal.repository.MovementRepository;
import com.paymybuddy.dal.repository.TransactionRepository;
import com.paymybuddy.dal.repository.UserRepository;
import com.paymybuddy.service.UserService;
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

    private User user1 = new User();
    private User user2 = new User();
    private Log log1 = new Log();
    private Log log2 = new Log();

    @BeforeAll
    public void initDataBase(){
        //Reinit de la base
        transactionRepository.deleteAll();
        movementRepository.deleteAll();
        logRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void addConnectionTestOk () {

        //User 1 - log et user
        log1.setEmail("email1@test.com");
        log1.setMdp("mdpTest1");
        user1.setFirstName("FirstName1");
        user1.setLastName("LastName1");
        user1 = userRepository.save(user1);
        log1.setUser(user1);
        log1 = logRepository.save(log1);

        //User 2 - log et user
        user2.setFirstName("FirstName2");
        user2.setLastName("LastName2");
        user2 = userRepository.save(user2);
        log2.setEmail("email2@test.com");
        log2.setMdp("mdpTest2");
        log2.setUser(user2);
        log2 = logRepository.save(log2);

        user1 = userService.addConnection(user1, "email2@test.com");
        assertEquals(1, user1.getConnections().size());
    }

    @Test
    public void addConnectionTestKo () {
        Assertions.assertThrows(NoSuchElementException.class,() -> userService.addConnection(user1, "emailko@test.com"));
    }
}
