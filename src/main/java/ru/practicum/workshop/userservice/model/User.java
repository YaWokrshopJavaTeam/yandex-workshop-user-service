package ru.practicum.workshop.userservice.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.workshop.userservice.model.enums.RegistrationType;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "about_me")
    private String aboutMe;

    @Column(name = "registration_type")
    @Enumerated(EnumType.STRING)
    private RegistrationType registrationType;

}
