package ru.practicum.workshop.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static ru.practicum.workshop.userservice.dto.UserDtoValidationConstants.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDto {

    @NotBlank(message = NAME_NOT_BLANK_ERROR_MESSAGE)
    @Size(min = NAME_MIN_SIZE, max = NAME_MAX_SIZE, message = NAME_SIZE_ERROR_MESSAGE)
    private String name;

    @NotBlank(message = EMAIL_NOT_BLANK_ERROR_MESSAGE)
    @Email(message = EMAIL_EMAIL_ERROR_MESSAGE)
    private String email;

    @NotBlank(message = PASSWORD_NOT_BLANK_ERROR_MESSAGE)
    @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE, message = PASSWORD_SIZE_ERROR_MESSAGE)
    private String password;

    @Size(max = ABOUT_ME_MAX_SIZE, message = ABOUT_ME_SIZE_ERROR_MESSAGE)
    private String aboutMe;

}
