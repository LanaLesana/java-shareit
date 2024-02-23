package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.item.model.Item;
import ru.practicum.item.repository.ItemRepository;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.ItemRequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    private Request request;
    private User user;
    private User otherUser;

    @BeforeEach
    public void setUp() {

        user = userRepository.save(User.builder()
                .id(1L)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build());
        otherUser = userRepository.save(User.builder()
                .id(2L)
                .name("Name")
                .email("iva1n@mail.ru")
                .build());

        Item item = itemRepository.save(Item.builder()
                .id(1L)
                .name("Щётка для обуви")
                .description("Стандартная щётка для обуви")
                .available(true)
                .request(1L)
                .ownerId(user.getId())
                .build());


        request = itemRequestRepository.save(Request.builder()
                .id(1L)
                .created(LocalDateTime.of(2023, 7, 9, 13, 56))
                .description("Хотел бы воспользоваться щёткой для обуви")
                .requestor(user)
                .items(Collections.singletonList(item))
                .build());
        System.out.println(request);
    }

    @AfterEach
    public void tearDown() {
        itemRequestRepository.deleteAll();
    }


    @Test
    void findAllByRequestorIdTest() {
        List<Request> requestList = itemRequestRepository.findAllByRequestorId(1L);
        assertNotNull(requestList);
    }

}
