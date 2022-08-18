package com.paymybuddy.service;

import com.paymybuddy.dal.entity.Log;
import com.paymybuddy.dal.repository.LogRepository;
import com.paymybuddy.web.dto.LogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
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

    @Transactional
    public Log logAndGetUser (LogDto logDto) {
        List<Log> logConnections = logRepository.findByEmail(logDto.getEmail());
        if (logConnections.size() == 1 && Objects.equals(logDto.getMdp(), logConnections.get(0).getMdp())) {
            return logConnections.get(0);
        } else {
            throw new NoSuchElementException("email or password unknown");
        }
    }
}
