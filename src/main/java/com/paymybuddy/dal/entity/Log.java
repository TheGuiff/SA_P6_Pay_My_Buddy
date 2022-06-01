package com.paymybuddy.dal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="log", schema = "public")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="email")
    private String email;

    @Column(name="mdp")
    private String mdp;

    @OneToOne
    @JoinColumn(name="user_id")
    private User utilisateur;

}
