package com.paymybuddy.webapp.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Connections")
public class Connections {

    @EmbeddedId
    private ConnectionsId id;
}
