package ru.practicum.workshop.userservice.dto;

import jakarta.validation.constraints.Pattern;
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
public class UpdateUserDto {

    @Pattern(regexp = "^\\s*\\S.*", message = NAME_PATTERN_ERROR_MESSAGE)
    @Size(min = NAME_MIN_SIZE, max = NAME_MAX_SIZE, message = NAME_SIZE_ERROR_MESSAGE)
    private String name;

    @Pattern(regexp = "^\\s*\\S.*", message = PASSWORD_PATTERN_ERROR_MESSAGE)
    @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE, message = PASSWORD_SIZE_ERROR_MESSAGE)
    private String password;

    @Size(max = ABOUT_ME_MAX_SIZE, message = ABOUT_ME_SIZE_ERROR_MESSAGE)
    private String aboutMe;

}
