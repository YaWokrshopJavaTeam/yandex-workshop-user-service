package ru.practicum.workshop.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.workshop.userservice.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByEmail(String email);
}
