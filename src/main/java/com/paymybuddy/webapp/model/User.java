package com.paymybuddy.webapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username",nullable = false,unique = true)
    private String username;

    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    // Relations

    @OneToOne(mappedBy = "user_id",cascade = CascadeType.ALL)
    private Account account;

    @OneToMany(mappedBy = "user_id", cascade = CascadeType.ALL)
    private List<Connections> connections; // All connections initiated by this user

    @OneToMany(mappedBy = "friend_id", cascade = CascadeType.ALL)
    private List<Connections> friends; // All connections where this user is the friend

    @OneToMany(mappedBy = "sender",cascade = CascadeType.ALL)
    private List<Transaction> sentTransactions;

    @OneToMany(mappedBy = "receiver",cascade = CascadeType.ALL)
    private List<Transaction> receivedTransactions;
}
