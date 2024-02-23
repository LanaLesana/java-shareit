package ru.practicum.shareit.request.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        User user1 = User.builder()
                .email("user1@gmail.com")
                .name("User1")
                .build();
        entityManager.persist(user1);

        User user2 = User.builder()
                .email("user2@gmail.com")
                .name("User2")
                .build();
        entityManager.persist(user2);

        Request request1 = new Request();
        request1.setRequester(user1);
        request1.setCreated(LocalDateTime.now().minusDays(1));
        entityManager.persist(request1);

        Request request2 = new Request();
        request2.setRequester(user2);
        request2.setCreated(LocalDateTime.now());
        entityManager.persist(request2);
    }

    @AfterEach
    void tearDown() {
        itemRequestRepository.deleteAll();
    }


    @Test
    void findByOwnerIdTest() {
        PageRequest pageable = PageRequest.of(0, 10);
        List<Request> requests = itemRequestRepository.findByOwnerId(1, pageable);
        assertTrue(requests.size() > 0, "Expected to find requests not owned by user ID 1");
        requests.forEach(request -> assertNotEquals(1, request.getRequester().getId(), "Request should not belong to user ID 1"));
    }
}
