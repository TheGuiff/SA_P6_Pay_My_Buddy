package com.paymybuddy.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewUserDto {

    public String email;
    public String mdp;
    public String firstName;
    public String lastName;

}
