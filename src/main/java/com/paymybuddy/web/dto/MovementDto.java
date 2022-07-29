package com.paymybuddy.web.dto;

import com.paymybuddy.dal.entity.TypeMovement;
import com.paymybuddy.dal.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class MovementDto {

    private TypeMovement type;
    private Double amount;
    private User user;

}
