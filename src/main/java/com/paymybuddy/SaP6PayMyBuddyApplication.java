package com.paymybuddy;

import com.paymybuddy.dal.entity.Log;
import com.paymybuddy.dal.entity.Movement;
import com.paymybuddy.dal.entity.Transaction;
import com.paymybuddy.dal.entity.User;
import com.paymybuddy.dal.repository.LogRepository;
import com.paymybuddy.dal.repository.MovementRepository;
import com.paymybuddy.dal.repository.TransactionRepository;
import com.paymybuddy.dal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;
import java.util.Optional;

@SpringBootApplication
public class SaP6PayMyBuddyApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SaP6PayMyBuddyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
