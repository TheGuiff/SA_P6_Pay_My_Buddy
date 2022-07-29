package com.paymybuddy.service;

import com.paymybuddy.dal.entity.Log;
import com.paymybuddy.dal.entity.Movement;
import com.paymybuddy.dal.entity.TypeMovement;
import com.paymybuddy.dal.entity.User;
import com.paymybuddy.dal.repository.LogRepository;
import com.paymybuddy.dal.repository.MovementRepository;
import com.paymybuddy.dal.repository.UserRepository;
import com.paymybuddy.web.dto.LogDto;
import com.paymybuddy.web.dto.MovementDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private MovementRepository movementRepository;

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

   public Optional<User> getUserById (Long id) {
        return userRepository.findById(id);
   }

   public User addUser (User user) {
        return userRepository.save(user);
   }

   public void deleteUserById (Long id) {
        userRepository.deleteById(id);
   }

   @Transactional
   public User addConnection (User user, String email) {
       List<Log> logConnection = logRepository.findByEmail(email);
       if (logConnection.size() == 1) {
           user.getConnections().add(logConnection.get(0).getUser());
           return userRepository.save(user);
       } else {
           throw new NoSuchElementException("unknown email address");
       }
   }

   @Transactional
   public Log logAndGetUser (LogDto logDto) {
        List<Log> logConnections = logRepository.findByEmail(logDto.getEmail());
        if (logConnections.size() == 1 && Objects.equals(logDto.getMdp(), logConnections.get(0).getMdp())) {
            return logConnections.get(0);
        } else {
            throw new NoSuchElementException("email or password unknown");
        }
   }

   @Transactional
    public User addMovementToUser (User user, Movement movement) {
       if (movement.getType() == TypeMovement.CREDIT) {
           user.setBalance(user.getBalance() + movement.getAmount());
       } else {
           user.setBalance(user.getBalance() - movement.getAmount());
       }
       user.getMovements().add(movement);
       return userRepository.save(user);
   }

}
