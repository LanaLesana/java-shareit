package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Integer, User> userHashMap = new HashMap<>();
    private int generatedUserId = 1;
    private UserDtoMapper userDtoMapper = new UserDtoMapper();

    @Override
    public UserDto getUserById(Integer id) {
        if (userHashMap.get(id) == null) {
            return null;
        } else {
            UserDto userDto = userDtoMapper.toUserDto(userHashMap.get(id));
            return userDto;
        }
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        if (!isDuplicateByEmail(userDto)) {
            userDto.setId(generatedUserId++);
            User user = userDtoMapper.fromUserDto(userDto);
            userHashMap.put(user.getId(), user);
            return userDto;
        } else {
            throw new ValidationException("Такой пользователь уже существует");
        }
    }

    @Override
    public UserDto updateUser(UserDto user, int userId) {
        if (userHashMap.get(userId) != null) {
            User userToUpdate = userHashMap.get(userId);
            List<UserDto> allUsers = getAllUsers();

            boolean isDuplicate = false;

            for (UserDto userInList : allUsers) {
                if (userInList.getEmail().equals(user.getEmail()) && userInList.getId() != userId) {
                    isDuplicate = true;
                }
            }
            if (isDuplicate) {
                throw new ValidationException("Такой e-mail присвоен другому пользователю");
            }
            if (user.getEmail() != null && !user.getEmail().equals(userToUpdate.getEmail())) {
                userToUpdate.setEmail(user.getEmail());
            }
            if (user.getName() != null && !user.getName().equals(userToUpdate.getName())) {
                userToUpdate.setName(user.getName());
            }

            UserDto userDto = userDtoMapper.toUserDto(userHashMap.get(userId));
            return userDto;

        } else {
            return null;
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        Collection<User> allUsers = userHashMap.values();
        List<User> allUsersList = new ArrayList<>(allUsers);
        List<UserDto> userDtoList = allUsersList.stream()
                .map(userDtoMapper::toUserDto)
                .collect(Collectors.toList());

        return userDtoList;
    }

    @Override
    public void deleteUser(int id) {
        if (userHashMap.get(id) == null) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Такого пользователя нет");
        } else {
            userHashMap.remove(id);
        }
    }

    @Override
    public boolean isDuplicateByEmail(UserDto userToCheck) {
        List<UserDto> users = getAllUsers();
        List<String> usersEmails = new ArrayList<>();
        for (UserDto userDto : users) {
            usersEmails.add(userDto.getEmail());
        }
        if (usersEmails.contains(userToCheck.getEmail())) {
            return true;
        } else {
            return false;
        }
    }
}
