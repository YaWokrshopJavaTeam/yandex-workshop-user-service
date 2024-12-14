package ru.practicum.workshop.userservice.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.workshop.userservice.dto.NewUserDto;
import ru.practicum.workshop.userservice.dto.UpdateUserDto;
import ru.practicum.workshop.userservice.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(NewUserDto newUserDto);

    UserDto autoCreateUser(NewUserDto newUserDto);

    UserDto updateUserData(UpdateUserDto updateUserDto, Long requesterId, String password);

    void deleteUser(Long requesterId, String password);

    UserDto getUser(Long userId, Long requesterId, String password);

    List<UserDto> getUsers(Pageable pageable);

}
