package ru.practicum.workshop.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.workshop.userservice.dto.NewUserDto;
import ru.practicum.workshop.userservice.dto.UpdateUserDto;
import ru.practicum.workshop.userservice.dto.UserDto;
import ru.practicum.workshop.userservice.service.UserService;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid NewUserDto newUserDto) {
        log.info("Request: create user, newUserDto={}", newUserDto);
        return userService.createUser(newUserDto);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUserData(@RequestBody @Valid UpdateUserDto updateUserDto,
                                  @RequestHeader("X-User-Id") Long requesterId,
                                  @RequestHeader("X-User-Password") String password) {
        log.info("Request: update user with id={}, updateUserDto={}", requesterId, updateUserDto);
        return userService.updateUserData(updateUserDto, requesterId, password);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@RequestHeader("X-User-Id") Long requesterId,
                           @RequestHeader("X-User-Password") String password) {
        log.info("Request: delete user with id={}", requesterId);
        userService.deleteUser(requesterId, password);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable Long userId,
                           @RequestHeader(value = "X-User-Id", required = false) Long requesterId,
                           @RequestHeader(value = "X-User-Password", required = false) String password) {
        log.info("Request: get user with id={}", userId);
        return userService.getUser(userId, requesterId, password);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsers(Pageable pageable) {
        log.info("Request: get users, page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        return userService.getUsers(pageable);
    }
}
