package ru.practicum.shareit.user.db;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface JpaUserRepository extends JpaRepository<User, Integer> {
    User findUserById(Integer id);

    void deleteUserById(Integer id);

    User findUserByEmail(String email);

    User save(User user);

    List<User> findAll();


}
