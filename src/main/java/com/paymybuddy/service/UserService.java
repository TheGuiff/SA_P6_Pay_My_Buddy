package com.paymybuddy.service;

import com.paymybuddy.dal.entity.Log;
import com.paymybuddy.dal.entity.User;
import com.paymybuddy.dal.repository.LogRepository;
import com.paymybuddy.dal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogRepository logRepository;

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

}
