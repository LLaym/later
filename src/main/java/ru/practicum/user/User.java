package ru.practicum.user;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    @Column(name = "registration_date")
    private Instant registrationDate;

    @Enumerated(EnumType.STRING)
    private UserState state;
}