package com.paymybuddy;

import com.paymybuddy.dal.entity.Log;
import com.paymybuddy.dal.entity.User;
import com.paymybuddy.dal.repository.LogRepository;
import com.paymybuddy.dal.repository.MovementRepository;
import com.paymybuddy.dal.repository.TransactionRepository;
import com.paymybuddy.dal.repository.UserRepository;
import com.paymybuddy.service.LogService;
import com.paymybuddy.web.dto.LogDto;
import com.paymybuddy.web.dto.NewUserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
//@TestPropertySource(locations="/application-test.properties") // Taper dans une autre base pour les tests
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //Me permet de faire mon Before all
public class LogServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LogRepository logRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    MovementRepository movementRepository;
    @Autowired
    LogService logService;

    private static final String email1 = "email1@test.com";
    private static final String mdp1 = "mdpTest1";
    private static final String firstName1 = "Firstname1";
    private static final String lastName1 = "Lastname1";

    private static final String email2 = "email2@test.com";
    private static final String mdp2 = "mdpTest2";
    private static final String firstname2 = "Firstname2";
    private static final String lastname2 = "Lastname2";

    private User user1 = new User();
    private Log log1 = new Log();
    private final LogDto logDto = new LogDto();
    private static final String emailKo = "emailKO@test.com";
    private static final String mdpKo = "mdpKoTest";
    private final NewUserDto newUserDto = new NewUserDto();

    @BeforeAll
    public void initDataBase() {
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
        user1 = userRepository.save(user1);
        log1.setUser(user1);
        log1 = logRepository.hashPasswordAndSave(log1);
    }

    @Test
    public void logInTestOk() {
        logDto.setEmail(email1);
        logDto.setMdp(mdp1);
        Log logOut = logService.logAndGetUser(logDto);
        assertEquals(email1, logOut.getEmail());
        assertEquals(logRepository.hashPassword(mdp1), logOut.getMdp());
        assertEquals(firstName1, logOut.getUser().getFirstName());
        assertEquals(lastName1, logOut.getUser().getLastName());
    }

    @Test
    public void logInKoUnknownUser() {
        logDto.setEmail(emailKo);
        logDto.setMdp(mdp1);
        Assertions.assertThrows(NoSuchElementException.class,() -> logService.logAndGetUser(logDto));
    }

    @Test
    public void logInKoWrongPassword() {
        logDto.setEmail(email1);
        logDto.setMdp(mdpKo);
        Assertions.assertThrows(NoSuchElementException.class,() -> logService.logAndGetUser(logDto));
    }

    @Test
    public void newUserOk() {
        newUserDto.setEmail(email2);
        newUserDto.setLastName(lastname2);
        newUserDto.setFirstName(firstname2);
        newUserDto.setMdp(mdp2);
        Log logOut = logService.newUserAndLog(newUserDto);
        assertEquals(email2, logOut.getEmail());
        assertEquals(logRepository.hashPassword(mdp2), logOut.getMdp());
        assertEquals(firstname2, logOut.getUser().getFirstName());
        assertEquals(lastname2, logOut.getUser().getLastName());
    }

    @Test
    public void newUserKoPasswordEmpty() {
        newUserDto.setEmail(email2);
        newUserDto.setLastName(lastname2);
        newUserDto.setFirstName(firstname2);
        newUserDto.setMdp("");
        Assertions.assertThrows(InputMismatchException.class,() -> logService.newUserAndLog(newUserDto));
    }

    @Test
    public void newUserKoEmailAlreadyExist() {
        newUserDto.setEmail(email1);
        newUserDto.setLastName(lastname2);
        newUserDto.setFirstName(firstname2);
        newUserDto.setMdp("");
        Assertions.assertThrows(InputMismatchException.class,() -> logService.newUserAndLog(newUserDto));
    }

}
