package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserStorage {
    UserDto getUserById(Integer id);

    UserDto addUser(UserDto user);

    UserDto updateUser(UserDto user, int userId);

    List<UserDto> getAllUsers();

    void deleteUser(int id);

    boolean isDuplicateByEmail(UserDto userToCheck);
}
