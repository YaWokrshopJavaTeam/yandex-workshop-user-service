package ru.practicum.workshop.userservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.workshop.userservice.dto.NewUserDto;
import ru.practicum.workshop.userservice.dto.UpdateUserDto;
import ru.practicum.workshop.userservice.dto.UserDto;
import ru.practicum.workshop.userservice.exception.AuthenticationException;
import ru.practicum.workshop.userservice.mapping.UserMapper;
import ru.practicum.workshop.userservice.model.User;
import ru.practicum.workshop.userservice.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto createUser(NewUserDto newUserDto) {
        User newUser = userRepository.save(userMapper.toUser(newUserDto));

        log.info("User added: {}", newUser);

        return userMapper.toUserDtoPrivate(newUser);
    }

    @Override
    @Transactional
    public UserDto updateUserData(UpdateUserDto updateUserDto, Long requesterId, String password) {
        Optional<User> userToUpdateOptional = userRepository.findById(requesterId);
        if (userToUpdateOptional.isEmpty()) {
            throw new EntityNotFoundException("User with id=" + requesterId + " not found.");
        }
        User userToUpdate = userToUpdateOptional.get();

        if (!userToUpdate.getPassword().equals(password)) {
            throw new AuthenticationException("Password for user with id=" + requesterId + " is invalid.");
        }

        User updatedUser = userRepository.save(userMapper.updateUser(userToUpdate, updateUserDto));

        log.info("User updated: {}", updatedUser);

        return userMapper.toUserDtoPrivate(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long requesterId, String password) {
        Optional<User> userToDeleteOptional = userRepository.findById(requesterId);
        if (userToDeleteOptional.isEmpty()) {
            throw new EntityNotFoundException("User with id=" + requesterId + " not found.");
        }
        User userToDelete = userToDeleteOptional.get();

        if (!userToDelete.getPassword().equals(password)) {
            throw new AuthenticationException("Password for user with id=" + requesterId + " is invalid.");
        }

        log.info("User deleted: id={}", userToDelete.getId());

        userRepository.deleteById(requesterId);
    }

    @Override
    public UserDto getUser(Long userId, Long requesterId, String password) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("User with id=" + userId + " not found.");
        }
        User user = userOptional.get();

        log.info("User got: id={}", user.getId());

        if (userId.equals(requesterId) && user.getPassword().equals(password)) {
            return userMapper.toUserDtoPrivate(user);
        } else {
            return userMapper.toUserDtoPublic(user);
        }
    }

    @Override
    public List<UserDto> getUsers(Pageable pageable) {
        List<User> users = userRepository.findAll(pageable).getContent();

        log.info("Users got: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

        return userMapper.toUserDtoPublic(users);
    }
}
