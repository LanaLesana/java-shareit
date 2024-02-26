package ru.practicum.user.service;


import ru.practicum.user.model.User;

import java.util.Collection;

public interface UserService {
    Collection<User> getAllUsers();

    User saveUser(User user);

    void deleteUser(Long userId);

    User updateUser(User user);

    User getUserById(Long id);
}
