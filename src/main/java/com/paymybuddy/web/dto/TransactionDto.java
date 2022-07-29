package com.paymybuddy.web.dto;

import com.paymybuddy.dal.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDto {

    private String description;
    private Double amount;
    private User userFrom;
    private User userTo;

}
