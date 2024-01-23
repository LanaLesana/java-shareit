package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping
@AllArgsConstructor
@Slf4j

public class UserController {
    @Autowired
    private final UserServiceImpl userService;

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{userId}")
    public UserDto getUserById(@PathVariable int userId) {
        return userService.getUserById(userId);
    }

    @PostMapping("/users")
    public UserDto addUser(@RequestBody @Valid UserDto userDto) {
        log.info("Adding item " + userDto.getName());
        System.out.println("Add user controller");
        return userService.addUser(userDto);
    }

    @PatchMapping("/users/{userId}")
    public UserDto updateUser(@RequestBody @Valid UserDto userDto, @PathVariable int userId) {
        return userService.updateUser(userDto, userId);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable int userId) {
        userService.deleteUserById(userId);
    }


}

