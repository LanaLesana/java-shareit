package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {
    @Autowired
    UserStorage userStorage;

    @Override
    public UserDto addUser(UserDto userDto) {
        isValidUser(userDto);
        return userStorage.addUser(userDto);
    }

    @Override
    public UserDto updateUser(UserDto userDto, int userId) {
        if (userStorage.updateUser(userDto, userId) == null) {
            throw new BadRequestException("Такого пользователя не существует");
        } else {
            return userStorage.getUserById(userId);
        }
    }

    @Override
    public UserDto getUserById(int id) {
        if (userStorage.getUserById(id) == null) {
            throw new BadRequestException("Такого пользователя не существует");
        } else {
            return userStorage.getUserById(id);
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public void deleteUserById(int id) {
        userStorage.deleteUser(id);
    }

    void isValidUser(UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().isBlank()) {
            throw new ValidationException("Не задано имя пользователя");
        } else if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new BadRequestException("Не задан e-mail пользователя");
        }
    }
}
