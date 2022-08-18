package com.paymybuddy.service;

import com.paymybuddy.dal.entity.Movement;
import com.paymybuddy.dal.entity.TypeMovement;
import com.paymybuddy.dal.repository.MovementRepository;
import com.paymybuddy.web.dto.MovementDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class MovementService {

    @Autowired
    private MovementRepository movementRepository;

    @Transactional
    public Movement newMovementService (MovementDto movementDto) throws Exception {
        if (movementDto.getAmount() > 0.0) {
            if (movementDto.getType() == TypeMovement.DEBIT && movementDto.getAmount() > movementDto.getUser().getBalance()) {
                throw new Exception("Account balance (" + movementDto.getUser().getBalance() + ") insufficient for debit (" + movementDto.getAmount() + ")");
            } else {
                Movement movement = new Movement();
                movement.setType(movementDto.getType());
                movement.setAmount(movementDto.getAmount());
                movement.setDateMovement(LocalDateTime.now());
                movement.setUser(movementDto.getUser());
                return movementRepository.save(movement);
            }
        } else {
            throw new Exception("Movement with no account");
        }
    }

}
