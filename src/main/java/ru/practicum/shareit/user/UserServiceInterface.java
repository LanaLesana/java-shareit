package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserServiceInterface {
    UserDto addUser(UserDto user);

    UserDto updateUser(UserDto user, int userId);

    UserDto getUserById(int id);

    List<UserDto> getAllUsers();

    void deleteUserById(int id);
}
