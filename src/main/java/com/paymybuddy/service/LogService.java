package com.paymybuddy.service;

import com.paymybuddy.dal.entity.Log;
import com.paymybuddy.dal.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public Iterable<Log> getLogs() {
        return logRepository.findAll();
    }

    public Optional<Log> getLogById (Long id) {
        return logRepository.findById(id);
    }

    public Log addLog (Log log) {
        return logRepository.save(log);
    }

    public void deleteLogById (Long id) {
        logRepository.deleteById(id);
    }
}
