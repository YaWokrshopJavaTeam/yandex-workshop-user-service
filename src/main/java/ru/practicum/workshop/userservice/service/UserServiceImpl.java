package ru.practicum.workshop.userservice.service;

import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.workshop.userservice.dto.AuthRegistrationDto;
import ru.practicum.workshop.userservice.dto.AutoUpdateUserDto;
import ru.practicum.workshop.userservice.dto.NewUserDto;
import ru.practicum.workshop.userservice.dto.ResponseWithUserId;
import ru.practicum.workshop.userservice.dto.UpdateUserDto;
import ru.practicum.workshop.userservice.dto.UserDto;
import ru.practicum.workshop.userservice.enums.RegistrationType;
import ru.practicum.workshop.userservice.exception.AuthenticationException;
import ru.practicum.workshop.userservice.mapping.UserMapper;
import ru.practicum.workshop.userservice.model.User;
import ru.practicum.workshop.userservice.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RegistrationClient registrationClient;

    @Override
    @Transactional
    public UserDto createUser(NewUserDto newUserDto) {
        User newUser = userRepository.save(userMapper.toUser(newUserDto, RegistrationType.MANUAL));

        log.info("User added: {}", newUser);

        return userMapper.toUserDtoPrivate(newUser);
    }

    @Override
    @Transactional
    public ResponseWithUserId autoCreateUser(NewUserDto newUserDto) {
        User newUser = userRepository.save(userMapper.toUser(newUserDto, RegistrationType.AUTO));

        log.info("Auto user added: {}", newUser);

        return new ResponseWithUserId(newUser.getId());
    }

    @Override
    @Transactional
    public UserDto updateUserData(UpdateUserDto updateUserDto, Long requesterId, String password) {
        User userToUpdate = userRepository.findById(requesterId).orElseThrow(
                () -> new AuthenticationException("You don't have access to update information about user with id="
                        + requesterId));
        if (!userToUpdate.getPassword().equals(password)) {
            throw new AuthenticationException("Password for user with id=" + requesterId + " is invalid.");
        }

        User updatedUser = userRepository.save(userMapper.updateUser(userToUpdate, updateUserDto));

        log.info("User updated: {}", updatedUser);

        return userMapper.toUserDtoPrivate(updatedUser);
    }

    @Override
    @Transactional
    public UserDto autoUpdateUserData(AutoUpdateUserDto autoUpdateUserDto, Long userId) {
        User userToUpdate = userRepository.getReferenceById(userId);

        User updatedUser = userRepository.save(userMapper.autoUpdateUser(userToUpdate, autoUpdateUserDto));

        log.info("Auto user updated: {}", updatedUser);

        return userMapper.toUserDtoPrivate(updatedUser);
    }

    @Override
    public void transferUserToManual(Long userId, AuthRegistrationDto authRegistrationDto) {
        User userForTransfer = userRepository.findById(userId).orElseThrow(
                () -> new AuthenticationException("Service don't have information about user with id=" + userId));
        if (userForTransfer.getRegistrationType().equals(RegistrationType.MANUAL)) {
            throw new AuthenticationException("Registration status of user with id=" + userId + " is already MANUAL.");
        }

        ResponseWithUserId responseWithUserId;
        try {
            responseWithUserId = registrationClient.confirmUser(authRegistrationDto.getId(), authRegistrationDto.getPassword());
        } catch (FeignException.NotFound e) {
            throw new AuthenticationException("Registration service don't have information about registration with id=" + authRegistrationDto.getId());
        }

        if (!(Objects.equals(responseWithUserId.getUserId(), userId))) {
            throw new AuthenticationException("Registration service don't have information about registration with id=" + authRegistrationDto.getId() +
                    " from user with id=" + userId);
        }
        userForTransfer.setRegistrationType(RegistrationType.MANUAL);
        userRepository.save(userForTransfer);
    }

    @Override
    @Transactional
    public void deleteUser(Long requesterId, String password) {
        User userToDelete = userRepository.findById(requesterId).orElseThrow(
                () -> new AuthenticationException("You don't have access to delete user with id="
                        + requesterId));
        if (!userToDelete.getPassword().equals(password)) {
            throw new AuthenticationException("Password for user with id=" + requesterId + " is invalid.");
        }

        log.info("User deleted: id={}", userToDelete.getId());

        userRepository.deleteById(requesterId);
    }

    @Override
    @Transactional
    public void autoDeleteUser(Long userId) {

        log.info("Auto user deleted: id={}", userId);

        userRepository.deleteById(userId);
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
