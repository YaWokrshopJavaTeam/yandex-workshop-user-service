package ru.practicum.workshop.userservice.dto;

public final class UserDtoValidationConstants {

    public static final String NAME_NOT_BLANK_ERROR_MESSAGE = "User name must be not null and not blank.";
    public static final String NAME_PATTERN_ERROR_MESSAGE = "User name must be not blank.";
    public static final int NAME_MIN_SIZE = 3;
    public static final int NAME_MAX_SIZE = 64;
    public static final String NAME_SIZE_ERROR_MESSAGE = "User name length must be between " + NAME_MIN_SIZE + " and " +
            NAME_MAX_SIZE + ".";

    public static final String EMAIL_NOT_BLANK_ERROR_MESSAGE = "User email must be not null and not blank.";
    public static final String EMAIL_EMAIL_ERROR_MESSAGE = "User email must be correct email.";

    public static final String PASSWORD_NOT_BLANK_ERROR_MESSAGE = "User password must be not null and not blank.";
    public static final String PASSWORD_PATTERN_ERROR_MESSAGE = "User password must be not blank.";
    public static final int PASSWORD_MIN_SIZE = 4;
    public static final int PASSWORD_MAX_SIZE = 64;
    public static final String PASSWORD_SIZE_ERROR_MESSAGE = "User password length must be between " +
            PASSWORD_MIN_SIZE + " and " + PASSWORD_MAX_SIZE + ".";

    public static final int ABOUT_ME_MAX_SIZE = 256;
    public static final String ABOUT_ME_SIZE_ERROR_MESSAGE = "User about me field must be less or equal to " +
            ABOUT_ME_MAX_SIZE + ".";

}
