package com.paymybuddy.dal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="description")
    private String description;

    @Column(name="date_transaction")
    private LocalDateTime dateTransaction;

    @Column(name="amount")
    private Double amount;

    @Column(name="commission")
    private Double commission;

    @ManyToOne()
    @JoinColumn(name="user_from")
    private User userFrom;

    @ManyToOne()
    @JoinColumn(name="user_to")
    private User userTo;

}
