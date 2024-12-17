package ru.practicum.workshop.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.workshop.userservice.controller.UserController;
import ru.practicum.workshop.userservice.dto.AuthRegistrationDto;
import ru.practicum.workshop.userservice.dto.NewUserDto;
import ru.practicum.workshop.userservice.dto.ResponseWithUserId;
import ru.practicum.workshop.userservice.dto.UpdateUserDto;
import ru.practicum.workshop.userservice.dto.UpdateUserFromRegistrationDto;
import ru.practicum.workshop.userservice.dto.UserDto;
import ru.practicum.workshop.userservice.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    public UserService userService;

    // Method "createUser" tests.
    @Test
    public void createUser_whenInputValid_thenSave() throws Exception {
        NewUserDto inputNewUserDto = NewUserDto.builder()
                .name("Yury")
                .email("yury@yandex.ru")
                .password("yurypass")
                .aboutMe("Good person.").build();

        UserDto outputUserDto = UserDto.builder()
                .id(1L)
                .name("Yury")
                .email("yury@yandex.ru")
                .password("yurypass")
                .aboutMe("Good person.").build();

        when(userService.createUser(any(NewUserDto.class))).thenReturn(outputUserDto);

        mockMvc.perform(post("/users")
                    .content(objectMapper.writeValueAsString(inputNewUserDto))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is(outputUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name", is(outputUserDto.getName())))
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.email", is(outputUserDto.getEmail())))
                .andExpect(jsonPath("$.password").exists())
                .andExpect(jsonPath("$.password", is(outputUserDto.getPassword())))
                .andExpect(jsonPath("$.aboutMe").exists())
                .andExpect(jsonPath("$.aboutMe", is(outputUserDto.getAboutMe())));
    }

    @Test
    public void createUser_whenNameInvalid_thenThrowException() throws Exception {
        NewUserDto inputNewUserDto = NewUserDto.builder()
                .name("Y")
                .email("yury@yandex.ru")
                .password("yurypass")
                .aboutMe("Good person.").build();

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(inputNewUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUser_whenEmailInvalid_thenThrowException() throws Exception {
        NewUserDto inputNewUserDto = NewUserDto.builder()
                .name("Yury")
                .email("yury.yandex.ru")
                .password("yurypass")
                .aboutMe("Good person.").build();

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(inputNewUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUser_whenPasswordInvalid_thenThrowException() throws Exception {
        NewUserDto inputNewUserDto = NewUserDto.builder()
                .name("Yury")
                .email("yury@yandex.ru")
                .password("yp")
                .aboutMe("Good person.").build();

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(inputNewUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // Method "updateUserData" tests.
    @Test
    public void updateUserData_whenInputValid_thenUpdate() throws Exception {
        UpdateUserDto inputUpdateUserDto = UpdateUserDto.builder()
                .name("Yuri")
                .password("yuripass")
                .aboutMe("Very good person.").build();

        UserDto outputUserDto = UserDto.builder()
                .id(1L)
                .name("Yuri")
                .email("yury@yandex.ru")
                .password("yuripass")
                .aboutMe("Very good person.").build();

        when(userService.updateUserData(any(UpdateUserDto.class), any(Long.class), any(String.class)))
                .thenReturn(outputUserDto);

        mockMvc.perform(patch("/users")
                        .header("X-User-Id", 1L)
                        .header("X-User-Password", "yurypass")
                        .content(objectMapper.writeValueAsString(inputUpdateUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is(outputUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name", is(outputUserDto.getName())))
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.email", is(outputUserDto.getEmail())))
                .andExpect(jsonPath("$.password").exists())
                .andExpect(jsonPath("$.password", is(outputUserDto.getPassword())))
                .andExpect(jsonPath("$.aboutMe").exists())
                .andExpect(jsonPath("$.aboutMe", is(outputUserDto.getAboutMe())));
    }

    @Test
    public void updateUserData_whenNameOutOfSize_thenThrowException() throws Exception {
        UpdateUserDto inputUpdateUserDto = UpdateUserDto.builder()
                .name("Yu").build();


        mockMvc.perform(patch("/users")
                        .header("X-User-Id", 1L)
                        .header("X-User-Password", "yurypass")
                        .content(objectMapper.writeValueAsString(inputUpdateUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUserData_whenNameIsBlank_thenThrowException() throws Exception {
        UpdateUserDto inputUpdateUserDto = UpdateUserDto.builder()
                .name("    ").build();


        mockMvc.perform(patch("/users")
                        .header("X-User-Id", 1L)
                        .header("X-User-Password", "yurypass")
                        .content(objectMapper.writeValueAsString(inputUpdateUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUserData_whenPasswordOutOfSize_thenThrowException() throws Exception {
        UpdateUserDto inputUpdateUserDto = UpdateUserDto.builder()
                .password("psw").build();


        mockMvc.perform(patch("/users")
                        .header("X-User-Id", 1L)
                        .header("X-User-Password", "yurypass")
                        .content(objectMapper.writeValueAsString(inputUpdateUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUserData_whenPasswordIsBlank_thenThrowException() throws Exception {
        UpdateUserDto inputUpdateUserDto = UpdateUserDto.builder()
                .password("    ").build();


        mockMvc.perform(patch("/users")
                        .header("X-User-Id", 1L)
                        .header("X-User-Password", "yurypass")
                        .content(objectMapper.writeValueAsString(inputUpdateUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // Method "deleteUser" tests.
    @Test
    public void deleteUser_whenInputValid_thenDelete() throws Exception {
        mockMvc.perform(delete("/users")
                        .header("X-User-Id", 1L)
                        .header("X-User-Password", "yurypass")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // Method "getUser" tests.
    @Test
    public void getUser_Anonymous_whenInputValid_thenReturn() throws Exception {
        UserDto outputUserDto = UserDto.builder()
                .id(1L)
                .name("Yury")
                .email("yury@yandex.ru")
                .aboutMe("Good person.").build();

        when(userService.getUser(any(Long.class), nullable(Long.class), nullable(String.class)))
                .thenReturn(outputUserDto);

        mockMvc.perform(get("/users/{userId}", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is(outputUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name", is(outputUserDto.getName())))
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.email", is(outputUserDto.getEmail())))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.aboutMe").exists())
                .andExpect(jsonPath("$.aboutMe", is(outputUserDto.getAboutMe())));
    }

    @Test
    public void getUser_BySelf_whenInputValid_thenReturn() throws Exception {
        UserDto outputUserDto = UserDto.builder()
                .id(1L)
                .name("Yury")
                .email("yury@yandex.ru")
                .password("yurypass")
                .aboutMe("Good person.").build();

        when(userService.getUser(any(Long.class), any(Long.class), any(String.class)))
                .thenReturn(outputUserDto);

        mockMvc.perform(get("/users/{userId}", 1L)
                        .header("X-User-Id", 1L)
                        .header("X-User-Password", "yurypass")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is(outputUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name", is(outputUserDto.getName())))
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.email", is(outputUserDto.getEmail())))
                .andExpect(jsonPath("$.password").exists())
                .andExpect(jsonPath("$.password", is(outputUserDto.getPassword())))
                .andExpect(jsonPath("$.aboutMe").exists())
                .andExpect(jsonPath("$.aboutMe", is(outputUserDto.getAboutMe())));
    }

    // Method "getUsers" tests.
    @Test
    public void getUsers_whenInputValid_thenReturn() throws Exception {
        UserDto outputUserDto = UserDto.builder()
                .id(1L)
                .name("Yury")
                .email("yury@yandex.ru")
                .aboutMe("Good person.").build();

        when(userService.getUsers(any(Pageable.class)))
                .thenReturn(List.of(outputUserDto));

        mockMvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].id", is(outputUserDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].name", is(outputUserDto.getName())))
                .andExpect(jsonPath("$[0].email").exists())
                .andExpect(jsonPath("$[0].email", is(outputUserDto.getEmail())))
                .andExpect(jsonPath("$[0].password").doesNotExist())
                .andExpect(jsonPath("$[0].aboutMe").exists())
                .andExpect(jsonPath("$[0].aboutMe", is(outputUserDto.getAboutMe())));
    }

    // Method "autoCreateUser" tests.
    @Test
    public void createUserInternal_whenInputValid_thenSave() throws Exception {
        NewUserDto inputNewUserDto = NewUserDto.builder()
                .name("Yury")
                .email("yury@yandex.ru")
                .password("yurypass")
                .aboutMe("Good person.").build();

        ResponseWithUserId responseWithUserId = new ResponseWithUserId(1L);

        when(userService.autoCreateUser(any(NewUserDto.class))).thenReturn(responseWithUserId);

        mockMvc.perform(post("/users/internal")
                        .content(objectMapper.writeValueAsString(inputNewUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.userId", is(responseWithUserId.getUserId()), Long.class));
    }

    // Method "autoUpdateUserData" tests.
    @Test
    public void updateInternalUserData_whenInputValid_thenUpdate() throws Exception {
        UpdateUserFromRegistrationDto updateUserFromRegistrationDto = new UpdateUserFromRegistrationDto("Yuri", "yury@yandex.ru");

        mockMvc.perform(patch("/users/internal")
                        .header("X-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(updateUserFromRegistrationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // Method "transferUserToManual" tests.
    @Test
    public void transferUserToManual_whenInputValid_thenUpdate() throws Exception {
        AuthRegistrationDto authRegistrationDto = new AuthRegistrationDto(1L, "dsffds");

        mockMvc.perform(put("/users/internal/to-manual")
                        .header("X-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(authRegistrationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // Method "autoDeleteUser" tests.
    @Test
    public void autoDeleteUser_whenInputValid_thenUpdate() throws Exception {

        mockMvc.perform(delete("/users/internal")
                        .header("X-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
