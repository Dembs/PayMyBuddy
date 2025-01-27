package com.paymybuddy.webapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Setter
@Getter
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username",nullable = false,unique = true)
    private String username;

    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @ToString.Exclude
    @JsonIgnore
    @Column(name = "password")
    private String password;

    // Relations

    @JsonManagedReference
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Account account;

    @JsonBackReference
    @ManyToMany(
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "connections",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<User> connections = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("ROLE_USER")); //Un seul role
    }

    @Override
    public String getUsername(){
        return this.email;
    }

    public String getRealUsername(){
        return this.username;
    }
}
