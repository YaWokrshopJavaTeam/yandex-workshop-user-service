package ru.practicum.workshop.userservice.mapping;

import org.mapstruct.*;
import ru.practicum.workshop.userservice.dto.NewUserDto;
import ru.practicum.workshop.userservice.dto.UpdateUserDto;
import ru.practicum.workshop.userservice.dto.UserDto;
import ru.practicum.workshop.userservice.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(NewUserDto newUserDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

    User updateUser(@MappingTarget User user, UpdateUserDto updateUserDto);

    UserDto toUserDtoPrivate(User user);

    @Named("toUserDtoPublic")
    @Mapping(target = "password", ignore = true)
    UserDto toUserDtoPublic(User user);

    @IterableMapping(qualifiedByName = "toUserDtoPublic")
    List<UserDto> toUserDtoPublic(List<User> users);

}
