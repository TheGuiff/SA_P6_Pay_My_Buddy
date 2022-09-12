package com.paymybuddy.service;

import com.paymybuddy.dal.entity.Log;
import com.paymybuddy.dal.entity.User;
import com.paymybuddy.dal.repository.LogRepository;
import com.paymybuddy.dal.repository.UserRepository;
import com.paymybuddy.web.dto.LogDto;
import com.paymybuddy.web.dto.NewUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Log logAndGetUser (LogDto logDto) {
        //Se connecter pour un user existant - sinon exception
        List<Log> logConnections = logRepository.findByEmail(logDto.getEmail());
        if (logConnections.size() == 1 && Objects.equals(logRepository.hashPassword(logDto.getMdp()), logConnections.get(0).getMdp())) {
            return logConnections.get(0);
        } else {
            throw new NoSuchElementException("email or password unknown");
        }
    }

    public Log newUserAndLog (NewUserDto newUserDto) {
        // Nouvel utilisateur - vérifie qu'il n'existe pas déjà et qu'un mdp est renseigné
        List<Log> logConnections = logRepository.findByEmail(newUserDto.getEmail());
        if (logConnections.size() == 1) { //Utilisateur existe déjà
            throw new InputMismatchException("email already exist in database");
        } else if (newUserDto.getMdp().length() == 0) {
            throw new InputMismatchException("password can't be empty");
        } else {
            Log log = new Log();
            User user = new User();
            log.setEmail(newUserDto.getEmail());
            log.setMdp(newUserDto.getMdp());
            user.setFirstName(newUserDto.getFirstName());
            user.setLastName(newUserDto.getLastName());
            user = userRepository.save(user);
            log.setUser(user);
            return logRepository.hashPasswordAndSave(log);
        }
    }

}
