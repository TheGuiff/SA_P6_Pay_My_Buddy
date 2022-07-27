package com.paymybuddy.dal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="movement")
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="date_movement")
    private LocalDateTime dateMovement;

    @Column(name="type")
    private TypeMovement type;

    @Column(name="amount")
    private Double amount;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

}
