package ru.practicum.shareit.user.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class JpaUserRepositoryTest {

    @Autowired
    private JpaUserRepository userRepository;

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");
        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId(), "User should have an id after saving.");
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void testFindUserById() {
        User user = new User();
        user.setEmail("findbyid@example.com");
        user.setName("FindById User");
        User savedUser = userRepository.save(user);

        User foundUser = userRepository.findUserById(savedUser.getId());
        assertNotNull(foundUser, "User should be found by ID.");
        assertEquals(savedUser.getEmail(), foundUser.getEmail(), "Emails should match.");
    }

    @Test
    public void testDeleteUserById() {
        User user = new User();
        user.setEmail("delete@example.com");
        user.setName("Delete User");
        User savedUser = userRepository.save(user);

        userRepository.deleteUserById(savedUser.getId());
        User foundUser = userRepository.findUserById(savedUser.getId());
        assertNull(foundUser, "User should be deleted and not found.");
    }

    @Test
    public void testFindUserByEmail() {
        User user = new User();
        user.setEmail("unique@example.com");
        user.setName("Unique User");
        userRepository.save(user);

        User foundUser = userRepository.findUserByEmail("unique@example.com");
        assertNotNull(foundUser, "User should be found by email.");
        assertEquals("unique@example.com", foundUser.getEmail(), "Emails should match.");
    }

    @Test
    public void testFindAllUsers() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setName("User One");
        userRepository.save(user1);

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setName("User Two");
        userRepository.save(user2);

        List<User> users = userRepository.findAll();
        assertTrue(users.size() >= 2, "Should find all users including the saved ones.");
    }
}
