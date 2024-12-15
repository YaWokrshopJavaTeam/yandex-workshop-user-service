package ru.practicum.workshop.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoUpdateUserDto {

    private String name;

    private String password;

    private String email;

}
