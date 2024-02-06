package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.db.JpaUserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private JpaUserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDtoMapper userDtoMapper = new UserDtoMapper();

    @Test
    void testAddUser() {
        UserDto userDto = UserDto.builder()
                .email("userdto1@gmail.com")
                .name("UsersDtoName")
                .id(1)
                .build();
        User user = userDtoMapper.fromUserDto(userDto);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto result = userService.addUser(userDto);

        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser() {
        int userId = 1;
        UserDto userDto = UserDto.builder()
                .email("userdto1@gmail.com")
                .name("UsersDtoName")
                .id(1)
                .build();
        User existingUser = User.builder()
                .id(1)
                .email("user1@gmail.com")
                .name("User1")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserDto result = userService.updateUser(userDto, userId);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetUserById_NotFound() {
        int userId = 1;
        when(userRepository.findUserById(userId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void testGetUserById_Success() {
        int userId = 1;
        User user = User.builder()
                .id(1)
                .email("user1@gmail.com")
                .name("User1")
                .build();

        when(userRepository.findUserById(userId)).thenReturn(user);

        UserDto result = userService.getUserById(userId);

        assertNotNull(result);
        verify(userRepository, times(2)).findUserById(userId);
    }
}