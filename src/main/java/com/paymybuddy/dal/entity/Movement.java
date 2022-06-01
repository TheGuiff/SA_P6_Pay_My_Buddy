package com.paymybuddy.dal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="movement", schema = "public")
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="date_movement")
    private String dateMovement;

    @Column(name="amount")
    private Double amount;

}
