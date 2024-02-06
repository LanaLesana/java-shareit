package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.db.JpaItemRepository;
import ru.practicum.shareit.user.db.JpaUserRepository;
import ru.practicum.shareit.booking.db.BookingStorage;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.validation.BookingValidation;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingStorage bookingStorage;

    @Mock
    private JpaItemRepository itemRepository;

    @Mock
    private JpaUserRepository userRepository;

    @Mock
    private BookingValidation bookingValidation;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void addBookingTest() {
        BookingDto bookingDto1 = BookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusMonths(1))
                .end(LocalDateTime.now().plusMonths(2))
                .build();

        Integer bookerId = 1;

        User user1 = User.builder()
                .id(1)
                .email("user1@gmail.com")
                .name("User1")
                .build();

        Item item1 = Item.builder()
                .id(1)
                .available(true)
                .description("Item1 Description")
                .name("Item1 name")
                .request(1)
                .owner(user1)
                .build();

        when(userRepository.findUserById(bookerId)).thenReturn(user1);
        when(itemRepository.findItemById(bookingDto1.getItemId())).thenReturn(item1);
        doNothing().when(bookingValidation).checkItemAvailable(any(), any());
        doNothing().when(bookingValidation).checkOwner(anyInt(), any());
        doNothing().when(bookingValidation).bookerValidation(any());

        Booking booking1 = Booking.builder()
                .booker(user1)
                .item(item1)
                .start(LocalDateTime.now().plusMonths(1))
                .end(LocalDateTime.now().plusMonths(2))
                .item(item1)
                .build();

        when(bookingStorage.addBooking(any())).thenReturn(booking1);

        Booking result = bookingService.addBooking(bookingDto1, bookerId);

        assertNotNull(result);
        verify(bookingStorage).addBooking(any());
    }

    @Test
    void updateBookingTest_Approved() {
        Integer bookingId = 1;
        Integer userId = 1;
        Boolean approved = true;

        User user1 = User.builder()
                .id(1)
                .email("user1@gmail.com")
                .name("User1")
                .build();

        Item item1 = Item.builder()
                .id(1)
                .available(true)
                .description("Item1 Description")
                .name("Item1 name")
                .request(1)
                .owner(user1)
                .build();

        Booking booking1 = Booking.builder()
                .booker(user1)
                .item(item1)
                .start(LocalDateTime.now().plusMonths(1))
                .end(LocalDateTime.now().plusMonths(2))
                .item(item1)
                .build();

        booking1.setStatus(Status.WAITING);

        when(bookingStorage.getBookingById(bookingId)).thenReturn(booking1);
        when(bookingStorage.addBooking(any(Booking.class))).thenAnswer(i -> i.getArguments()[0]);
        doNothing().when(bookingValidation).checkUpdateBooking(userId, approved, booking1);
        doNothing().when(bookingValidation).checkIdBookerUpdate(approved, booking1);

        Booking updatedBooking = bookingService.updateBooking(bookingId, userId, approved);

        assertEquals(Status.APPROVED, updatedBooking.getStatus());
        verify(bookingStorage).addBooking(booking1);
    }

    @Test
    void getBookingTest() {
        User user1 = User.builder()
                .id(1)
                .email("user1@gmail.com")
                .name("User1")
                .build();

        Item item1 = Item.builder()
                .id(1)
                .available(true)
                .description("Item1 Description")
                .name("Item1 name")
                .request(1)
                .owner(user1)
                .build();
        Booking booking1 = Booking.builder()
                .booker(user1)
                .item(item1)
                .start(LocalDateTime.now().plusMonths(1))
                .end(LocalDateTime.now().plusMonths(2))
                .item(item1)
                .build();
        Integer bookingId = 1;
        booking1.setId(bookingId);

        when(bookingStorage.getBookingById(bookingId)).thenReturn(booking1);
        doNothing().when(bookingValidation).checkBooking(booking1);
        doNothing().when(bookingValidation).checkOwnerAndBookerMatch(user1.getId(), booking1);
        when(bookingStorage.getBookingsByUser(user1.getId())).thenReturn(List.of(booking1));
        when(bookingStorage.getBookingByOwner(user1.getId())).thenReturn(List.of(booking1));

        // Execute the method under test
        Booking result = bookingService.getBooking(bookingId, user1.getId());

        assertNotNull(result, "The booking should not be null");
        assertEquals(bookingId, result.getId(), "The booking ID should match the requested ID");

        verify(bookingStorage, times(2)).getBookingById(bookingId);
        verify(bookingValidation).checkBooking(booking1);
        verify(bookingValidation).checkOwnerAndBookerMatch(user1.getId(), booking1);
        verify(bookingStorage).getBookingsByUser(user1.getId());
        verify(bookingStorage).getBookingByOwner(user1.getId());
    }

    @Test
    void getAllBookingUsers_WhenFromIsNegative_ShouldThrowBadRequestException() {
        Integer userId = 1;
        int from = -1;
        int size = 10;

        BadRequestException thrown = assertThrows(
                BadRequestException.class,
                () -> bookingService.getAllBookingUsers(userId, from, size),
                "Expected getAllBookingUsers to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Не может быть меньше нуля"));
    }

    @Test
    void getAllBookingUsers_WhenUserNotFound_ShouldThrowNotFoundException() {
        Integer userId = 2;
        int from = 0;
        int size = 10;

        when(userRepository.findUserById(userId)).thenReturn(null);

        NotFoundException thrown = assertThrows(
                NotFoundException.class,
                () -> bookingService.getAllBookingUsers(userId, from, size),
                "Expected getAllBookingUsers to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Такого пользователя нет"));
    }

    @Test
    void getAllBookingUsers_SuccessfulRetrieval() {
        Integer userId = 1;
        int from = 0;
        int size = 10;
        User user = new User();
        user.setId(userId);

        List<Booking> bookings = new ArrayList<>();

        when(userRepository.findUserById(userId)).thenReturn(user);
        when(bookingStorage.getAllBookingUsers(eq(userId), any(Pageable.class))).thenReturn(bookings);

        List<Booking> result = bookingService.getAllBookingUsers(userId, from, size);

        assertNotNull(result, "Bookings should not be null");
        assertEquals(bookings, result, "Expected bookings to match the mock response");
    }

    @Test
    void getAllBookingUsers_ValidatesUserCorrectly() {
        Integer userId = 1;
        int from = 0;
        int size = 10;
        User user = new User();
        user.setId(userId);

        when(userRepository.findUserById(userId)).thenReturn(user);
        doNothing().when(bookingValidation).checkBookerOrOwnerUser(user);

        assertDoesNotThrow(() -> bookingService.getAllBookingUsers(userId, from, size));

        verify(bookingValidation).checkBookerOrOwnerUser(user);
    }


}
