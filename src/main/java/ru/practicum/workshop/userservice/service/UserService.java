package ru.practicum.workshop.userservice.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.workshop.userservice.dto.AuthRegistrationDto;
import ru.practicum.workshop.userservice.dto.UpdateUserFromRegistrationDto;
import ru.practicum.workshop.userservice.dto.NewUserDto;
import ru.practicum.workshop.userservice.dto.ResponseWithUserId;
import ru.practicum.workshop.userservice.dto.UpdateUserDto;
import ru.practicum.workshop.userservice.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(NewUserDto newUserDto);

    ResponseWithUserId autoCreateUser(NewUserDto newUserDto);

    UserDto updateUserData(UpdateUserDto updateUserDto, Long requesterId, String password);

    void autoUpdateUserData(UpdateUserFromRegistrationDto updateUserFromRegistrationDto, Long userId);

    void transferUserToManual(Long userId, AuthRegistrationDto authRegistrationDto);

    void deleteUser(Long requesterId, String password);

    void autoDeleteUser(Long userId);

    UserDto getUser(Long userId, Long requesterId, String password);

    List<UserDto> getUsers(Pageable pageable);

}
