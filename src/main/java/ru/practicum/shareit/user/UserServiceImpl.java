package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.db.JpaUserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserServiceInterface {
    @Autowired
    private final JpaUserRepository userRepository;
    private final UserDtoMapper userDtoMapper = new UserDtoMapper();

    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {
        isValidUser(userDto);
        isDuplicatePlusId(userDto);
        User user = userDtoMapper.fromUserDto(userDto);
        return userDtoMapper.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, int userId) {
        log.info("input Data {}", userDto);
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(HttpStatus.BAD_REQUEST, "Такого пользователя не существует" + userId));

        if (userDto.getEmail() != null && userDto.getName() == null) {
            User userDuplicateByEmail = userRepository.findUserByEmail(userDto.getEmail());
            log.info("Found user by email {}", userDto.getEmail());
            if (userDuplicateByEmail != null) {
                if (userDuplicateByEmail.getId() != userId) {
                    log.info("Ids don't match", userDuplicateByEmail.getId());
                    throw new ConflictException("Пользователь с таким e-mail уже существует.");
                }
            }
        }

        User receivedUser = userDtoMapper.fromUserDto(userDto);

        if (receivedUser.getEmail() != null && !receivedUser.getEmail().isBlank()) {
            existingUser.setEmail(receivedUser.getEmail());
        }
        if (receivedUser.getName() != null && !receivedUser.getName().isBlank()) {
            existingUser.setName(receivedUser.getName());
        }
        return userDtoMapper.toUserDto(userRepository.save(existingUser));
    }

    @Override
    @Transactional
    public UserDto getUserById(int id) {
        if (userRepository.findUserById(id) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        } else {
            return userDtoMapper.toUserDto(userRepository.findUserById(id));
        }
    }

    @Override
    @Transactional
    public List<UserDto> getAllUsers() {
        Collection<User> allUsers = userRepository.findAll();
        List<User> allUsersList = new ArrayList<>(allUsers);
        List<UserDto> userDtoList = allUsersList.stream()
                .map(userDtoMapper::toUserDto)
                .collect(Collectors.toList());
        return userDtoList;
    }

    @Override
    @Transactional
    public void deleteUserById(int id) {
        log.info("Deleting user with ID {}", id);
        userRepository.deleteUserById(id);
    }

    void isValidUser(UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().isBlank()) {
            throw new ValidationException("Не задано имя пользователя");
        } else if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Не задан e-mail пользователя");
        }
    }

    public void isDuplicatePlusId(UserDto userDto) {
        if (userRepository.findUserByEmail(userDto.getEmail()) != null) {
            userRepository.save(new User());
            throw new ConflictException("Такой пользователь уже усуществует");
        }
    }

    public boolean isDuplicateBool(UserDto userDto) {
        if (userRepository.findUserByEmail(userDto.getEmail()) != null) {
            return true;
        } else {
            return false;
        }
    }
}
