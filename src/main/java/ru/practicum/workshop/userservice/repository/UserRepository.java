package ru.practicum.workshop.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.workshop.userservice.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getUserByEmail(String email);
}
