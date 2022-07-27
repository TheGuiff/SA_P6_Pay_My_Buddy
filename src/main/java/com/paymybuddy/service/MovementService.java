package com.paymybuddy.service;

import com.paymybuddy.dal.entity.Movement;
import com.paymybuddy.dal.repository.MovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MovementService {

    @Autowired
    private MovementRepository movementRepository;

    public Iterable<Movement> getMovements() {
        return movementRepository.findAll();
    }

    public Optional<Movement> getMovementById (Long id) {
        return movementRepository.findById(id);
    }

    public Movement addMovement(Movement movement) {
        return  movementRepository.save(movement);
    }

    public void deleteMovementById(Long id) {
        movementRepository.deleteById(id);
    }

}
