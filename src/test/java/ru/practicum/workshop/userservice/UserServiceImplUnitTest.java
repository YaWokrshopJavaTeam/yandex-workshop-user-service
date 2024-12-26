package ru.practicum.workshop.userservice;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.workshop.userservice.dto.NewUserDto;
import ru.practicum.workshop.userservice.dto.UpdateUserDto;
import ru.practicum.workshop.userservice.dto.UserDto;
import ru.practicum.workshop.userservice.model.enums.RegistrationType;
import ru.practicum.workshop.userservice.exception.AuthenticationException;
import ru.practicum.workshop.userservice.mapping.UserMapper;
import ru.practicum.workshop.userservice.model.User;
import ru.practicum.workshop.userservice.repository.UserRepository;
import ru.practicum.workshop.userservice.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplUnitTest {

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    // Method "createUser" tests.
    @Test
    public void createUser_whenInputValid_thenSave() {
        NewUserDto inputNewUserDto = NewUserDto.builder()
                .name("Yury")
                .email("yury@yandex.ru")
                .password("yurypass")
                .aboutMe("Good person.").build();

        User repositoryOutputUser = User.builder()
                .id(1L)
                .name("Yury")
                .email("yury@yandex.ru")
                .password("yurypass")
                .aboutMe("Good person.").build();

        UserDto expectedUserDto = UserDto.builder()
                .id(1L)
                .name("Yury")
                .email("yury@yandex.ru")
                .password("yurypass")
                .aboutMe("Good person.").build();

        when(userRepository.save(any(User.class))).thenReturn(repositoryOutputUser);

        UserDto actualUserDto = userService.createUser(inputNewUserDto);

        assertThat(actualUserDto, equalTo(expectedUserDto));
    }

    @Test
    public void createUser_whenEmailAlreadyExists_thenThrowException() {
        NewUserDto inputNewUserDto = NewUserDto.builder()
                .name("Yury")
                .email("yury@yandex.ru")
                .password("yurypass")
                .aboutMe("Good person.").build();

        when(userRepository.save(any(User.class)))
                .thenThrow(new DataIntegrityViolationException("Constraint violation: unique email."));

        assertThrows(DataIntegrityViolationException.class, () -> userService.createUser(inputNewUserDto));
    }

    // Method "updateUserData" tests.
    @Test
    public void updateUserData_whenInputValid_thenUpdate() {
        UpdateUserDto inputUpdateUserDto = UpdateUserDto.builder()
                .name("Yuri")
                .password("yuripass")
                .aboutMe("Very good person.").build();
        Long requesterId = 1L;
        String password = "yurypass";

        User repositoryOutputUser = User.builder()
                .id(1L)
                .name("Yury")
                .email("yury@yandex.ru")
                .password("yurypass")
                .aboutMe("Good person.").build();

        User repositorySavedUser = User.builder()
                .id(1L)
                .name("Yuri")
                .email("yury@yandex.ru")
                .password("yuripass")
                .aboutMe("Very good person.").build();

        UserDto expectedUserDto = UserDto.builder()
                .id(1L)
                .name("Yuri")
                .email("yury@yandex.ru")
                .password("yuripass")
                .aboutMe("Very good person.").build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(repositoryOutputUser));
        when(userRepository.save(any(User.class))).thenReturn(repositorySavedUser);

        UserDto actualUserDto = userService.updateUserData(inputUpdateUserDto, requesterId, password);

        assertThat(actualUserDto, equalTo(expectedUserDto));
    }

    @Test
    public void updateUserData_whenNoUserFound_thenThrowException() {
        UpdateUserDto inputUpdateUserDto = UpdateUserDto.builder()
                .name("Yuri")
                .password("yuripass")
                .aboutMe("Very good person.").build();
        Long requesterId = 1L;
        String password = "yurypass";

        when(userRepository.findById(requesterId)).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> userService.updateUserData(inputUpdateUserDto, requesterId, password));
    }

    @Test
    public void updateUserData_whenPasswordIncorrect_thenThrowException() {
        UpdateUserDto inputUpdateUserDto = UpdateUserDto.builder()
                .name("Yuri")
                .password("yuripass")
                .aboutMe("Very good person.").build();
        Long requesterId = 1L;
        String password = "yurapass";

        User repositoryOutputUser = User.builder()
                .id(1L)
                .name("Yury")
                .email("yury@yandex.ru")
                .password("yurypass")
                .aboutMe("Good person.").build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(repositoryOutputUser));

        assertThrows(AuthenticationException.class, () -> userService.updateUserData(inputUpdateUserDto, requesterId, password));
    }

    // Method "deleteUser" tests.
    @Test
    public void deleteUser_whenInputValid_thenDelete() {
        Long requesterId = 1L;
        String password = "yurypass";

        User repositoryOutputUser = User.builder()
                .id(1L)
                .name("Yury")
                .email("yury@yandex.ru")
                .password("yurypass")
                .aboutMe("Good person.").build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(repositoryOutputUser));

        userService.deleteUser(requesterId, password);

        verify(userRepository).deleteById(requesterId);
    }

    // Method "deleteUser" tests.
    @Test
    public void autoDeleteUser_whenInputValid_thenDelete() {
        Long requesterId = 1L;

        Optional<User> repositoryOutputUser = Optional.of(User.builder()
                .id(1L)
                .name("Yury")
                .email("yury@yandex.ru")
                .password("yurypass")
                .aboutMe("Good person.")
                .registrationType(RegistrationType.AUTO).build());

        when(userRepository.findById(requesterId)).thenReturn(repositoryOutputUser);

        userService.autoDeleteUser(requesterId);

        verify(userRepository).deleteById(requesterId);
    }

    @Test
    public void deleteUser_whenNoUserFound_thenThrowException() {
        Long requesterId = 1L;
        String password = "yurypass";

        when(userRepository.findById(requesterId)).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> userService.deleteUser(requesterId, password));
    }

    @Test
    public void deleteUser_whenPasswordIncorrect_thenThrowException() {
        Long requesterId = 1L;
        String password = "yurapass";

        User repositoryOutputUser = User.builder()
                .id(1L)
                .name("Yury")
                .email("yury@yandex.ru")
                .password("yurypass")
                .aboutMe("Good person.").build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(repositoryOutputUser));

        assertThrows(AuthenticationException.class, () -> userService.deleteUser(requesterId, password));
    }

    // Method "getUser" tests.
    @Test
    public void getUser_Anonymous_whenInputValid_thenReturn() {
        Long userId = 1L;

        User repositoryOutputUser = User.builder()
                .id(1L)
                .name("Yury")
                .email("yury@yandex.ru")
                .password("yurypass")
                .aboutMe("Good person.").build();

        UserDto expectedUserDto = UserDto.builder()
                .id(1L)
                .name("Yury")
                .email("yury@yandex.ru")
                .aboutMe("Good person.").build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(repositoryOutputUser));

        UserDto actualUserDto = userService.getUser(userId, null, null);

        assertThat(actualUserDto, equalTo(expectedUserDto));
    }

    @Test
    public void getUser_BySelf_whenInputValid_thenReturn() {
        Long userId = 1L;
        Long requesterId = 1L;
        String password = "yurypass";

        User repositoryOutputUser = User.builder()
                .id(1L)
                .name("Yury")
                .email("yury@yandex.ru")
                .password("yurypass")
                .aboutMe("Good person.").build();

        UserDto expectedUserDto = UserDto.builder()
                .id(1L)
                .name("Yury")
                .email("yury@yandex.ru")
                .password("yurypass")
                .aboutMe("Good person.").build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(repositoryOutputUser));

        UserDto actualUserDto = userService.getUser(userId, requesterId, password);

        assertThat(actualUserDto, equalTo(expectedUserDto));
    }

    @Test
    public void getUser_Anonymous_whenNoUserFound_thenThrowException() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUser(userId, null, null));
    }

    // Method "getUsers" tests.
    @Test
    public void getUsers_thenReturn() {
        User repositoryOutputUser1 = User.builder()
                .id(1L)
                .name("Yury")
                .email("yury@yandex.ru")
                .password("yurypass")
                .aboutMe("Good person.").build();

        User repositoryOutputUser2 = User.builder()
                .id(2L)
                .name("Denis")
                .email("denis@yandex.ru")
                .password("denispass")
                .aboutMe("Smart person.").build();

        List<User> repositoryOutputUsers = List.of(repositoryOutputUser1, repositoryOutputUser2);

        UserDto expectedUserDto1 = UserDto.builder()
                .id(1L)
                .name("Yury")
                .email("yury@yandex.ru")
                .aboutMe("Good person.").build();

        UserDto expectedUserDto2 = UserDto.builder()
                .id(2L)
                .name("Denis")
                .email("denis@yandex.ru")
                .aboutMe("Smart person.").build();

        List<UserDto> expectedUserDtoList = List.of(expectedUserDto1, expectedUserDto2);

        when(userRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(repositoryOutputUsers, PageRequest.of(0, 10), 2));

        List<UserDto> actualUserDtoList = userService.getUsers(PageRequest.of(0, 10));

        assertThat(actualUserDtoList, equalTo(expectedUserDtoList));
    }
}
