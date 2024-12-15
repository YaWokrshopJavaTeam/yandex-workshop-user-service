package ru.practicum.workshop.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ru.practicum.workshop.userservice.enums.RegistrationType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserDto {

    private Long id;

    private String name;

    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    private String aboutMe;

    private RegistrationType registrationType;

}
