package com.paymybuddy.dal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="firstname")
    private String firstName;

    @Column(name="lastname")
    private String lastName;

    @Column(name="email")
    private String email;

    @Column(name="balance")
    private Double balance;

    @OneToMany(mappedBy = "user")
    private List<Movement> movements = new ArrayList<>();

    @OneToMany(mappedBy = "userFrom")
    List<Transaction> transactions = new ArrayList<>();

    @ManyToMany(
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "connection",
            joinColumns = @JoinColumn(name = "user_id1"),
            inverseJoinColumns = @JoinColumn(name = "user_id2")
    )
    List<User> connections = new ArrayList<>();

}
