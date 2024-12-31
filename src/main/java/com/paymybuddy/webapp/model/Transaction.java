package com.paymybuddy.webapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Setter
@Getter
@Table(name="Transaction")

public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender",referencedColumnName = "id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver",referencedColumnName = "id")
    private User receiver;

    @Column(name = "type",nullable = false)
    private String type;

    @Column(name = "description",nullable = false)
    private String description;

    @Column(name = "amount",nullable = false)
    private double amount;

    @Column(name = "date",nullable = false)
    private LocalDateTime date;
}
