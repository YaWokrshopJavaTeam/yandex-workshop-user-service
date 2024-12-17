package ru.practicum.workshop.userservice.mapping;

import org.mapstruct.*;
import ru.practicum.workshop.userservice.dto.UpdateUserFromRegistrationDto;
import ru.practicum.workshop.userservice.dto.NewUserDto;
import ru.practicum.workshop.userservice.dto.UpdateUserDto;
import ru.practicum.workshop.userservice.dto.UserDto;
import ru.practicum.workshop.userservice.enums.RegistrationType;
import ru.practicum.workshop.userservice.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(NewUserDto newUserDto, RegistrationType registrationType);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User updateUser(@MappingTarget User user, UpdateUserDto updateUserDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User autoUpdateUser(@MappingTarget User user, UpdateUserFromRegistrationDto updateUserFromRegistrationDto);

    UserDto toUserDtoPrivate(User user);

    @Named("toUserDtoPublic")
    @Mapping(target = "password", ignore = true)
    UserDto toUserDtoPublic(User user);

    @IterableMapping(qualifiedByName = "toUserDtoPublic")
    List<UserDto> toUserDtoPublic(List<User> users);

}
