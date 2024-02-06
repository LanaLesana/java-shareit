package ru.practicum.shareit.booking.db;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JpaBookingRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaBookingRepository jpaBookingRepository;

    @Test
    @Transactional
    void getBookingByIdOrderByStartTest() {
        User user1 = User.builder()
                //.id(1)
                .email("user1@gmail.com")
                .name("User1")
                .build();
        entityManager.persist(user1);

        Item item1 = Item.builder()
                //.id(1)
                .available(true)
                .description("Item1 Description")
                .name("Item1 name")
                .request(1)
                .owner(user1)
                .build();
        entityManager.persist(item1);

        Booking booking1 = Booking.builder()
                .item(item1)
                .start(LocalDateTime.now().plusMonths(1))
                .end(LocalDateTime.now().plusMonths(2))
                .build();
        //booking1.setId(1);
        entityManager.persist(booking1);
        entityManager.flush();

        Booking found = jpaBookingRepository.getBookingByIdOrderByStart(booking1.getId());
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(booking1.getId());
    }

    @Test
    void findAllBookingsWithItemAndUserByIdTest() {
        User user1 = User.builder()
                .email("user1@gmail.com")
                .name("User1")
                .build();
        entityManager.persist(user1);
        entityManager.flush();

        Item item1 = Item.builder()
                .available(true)
                .description("Item1 Description")
                .name("Item1 name")
                .request(1)
                .owner(user1)
                .build();
        entityManager.persist(item1);
        entityManager.flush();

        Booking booking1 = Booking.builder()
                .item(item1)
                .booker(user1)
                .status(Status.WAITING)
                .start(LocalDateTime.of(2024, Month.FEBRUARY, 15, 10, 0))
                .end(LocalDateTime.of(2024, Month.MARCH, 15, 10, 0))
                .build();

        entityManager.persist(booking1);
        entityManager.flush();

        Booking found = jpaBookingRepository.findAllBookingsWithItemAndUserById(booking1.getId());
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(booking1.getId());
    }

    @Test
    void findAllByBooker_IdOrderByStartDescTest() {
        User user1 = User.builder()
                .email("user1@gmail.com")
                .name("User1")
                .build();
        entityManager.persist(user1);
        entityManager.flush();

        Item item1 = Item.builder()
                .available(true)
                .description("Item1 Description")
                .name("Item1 name")
                .request(1)
                .owner(user1)
                .build();
        entityManager.persist(item1);
        entityManager.flush();

        Booking booking1 = Booking.builder()
                .item(item1)
                .booker(user1)
                .status(Status.WAITING)
                .start(LocalDateTime.of(2024, Month.FEBRUARY, 15, 10, 0))
                .end(LocalDateTime.of(2024, Month.MARCH, 15, 10, 0))
                .build();

        entityManager.persist(booking1);
        entityManager.flush();

        Booking booking2 = Booking.builder()
                .item(item1)
                .booker(user1)
                .status(Status.WAITING)
                .start(LocalDateTime.of(2024, Month.MAY, 15, 10, 0))
                .end(LocalDateTime.of(2024, Month.JUNE, 15, 10, 0))
                .build();
        entityManager.persist(booking2);
        entityManager.flush();

        Pageable pageable = PageRequest.of(0, 10);
        List<Booking> bookings = jpaBookingRepository.findAllByBooker_IdOrderByStartDesc(user1.getId(), pageable);
        assertThat(bookings).hasSize(2);
    }
}