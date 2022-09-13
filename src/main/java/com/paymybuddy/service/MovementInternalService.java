package com.paymybuddy.service;

import com.paymybuddy.dal.entity.Movement;
import com.paymybuddy.dal.entity.User;
import com.paymybuddy.web.dto.MovementDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

@Service
public class MovementInternalService {

    @Autowired
    MovementService movementService;

    @Autowired
    UserService userService;

    @Required
    public Movement newMovementInternalService (MovementDto movementDto) throws Exception {
        Movement movement = movementService.newMovementService(movementDto);
        User user = userService.addMovementToUser(movementDto.getUser(), movement);
        movement.setUser(user);
        return movement;
    }

}
