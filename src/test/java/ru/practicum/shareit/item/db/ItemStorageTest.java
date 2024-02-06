package ru.practicum.shareit.item.db;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.db.JpaBookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.comment.JpaCommentRepository;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class ItemStorageTest {

    @Mock
    private JpaItemRepository jpaItemRepository;

    @Mock
    private JpaBookingRepository jpaBookingRepository;

    @Mock
    private JpaCommentRepository jpaCommentRepository;

    @InjectMocks
    private ItemStorage itemStorage;

    @Test
    void addItemTest() {
        User user1 = User.builder()
                .email("user1@gmail.com")
                .name("User1")
                .build();
        Item item = Item.builder()
                .available(true)
                .description("Item1 Description")
                .name("Item1 name")
                .request(1)
                .owner(user1)
                .build();
        when(jpaItemRepository.save(any(Item.class))).thenReturn(item);

        Item result = itemStorage.addItem(item);
        assertEquals(item, result);
        verify(jpaItemRepository).save(item);
    }

    @Test
    void getItemTest() {
        Integer id = 1;
        User user1 = User.builder()
                .email("user1@gmail.com")
                .name("User1")
                .build();
        Item expectedItem = Item.builder()
                .available(true)
                .description("Item1 Description")
                .name("Item1 name")
                .request(1)
                .owner(user1)
                .build();
        when(jpaItemRepository.findItemById(id)).thenReturn(expectedItem);

        Item result = itemStorage.getItem(id);

        assertEquals(expectedItem, result);
        verify(jpaItemRepository).findItemById(id);
    }

    @Test
    void getItemAndBooking_WithNextAndLastBookings() {
        Integer itemId = 1, ownerId = 1;
        LocalDateTime now = LocalDateTime.now();

        User owner = User.builder()
                .email("user1@gmail.com")
                .name("User1")
                .build();
        Item item = Item.builder()
                .available(true)
                .description("Item1 Description")
                .name("Item1 name")
                .request(1)
                .owner(owner)
                .build();

        item.setId(itemId);

        Booking nextBooking = new Booking();
        nextBooking.setId(2);
        nextBooking.setStart(now.plusDays(1));
        nextBooking.setEnd(now.plusDays(2));

        Booking lastBooking = new Booking();
        lastBooking.setId(3);
        lastBooking.setStart(now.minusDays(2));
        lastBooking.setEnd(now.minusDays(1));

        when(jpaItemRepository.findItemById(itemId)).thenReturn(item);
        when(jpaBookingRepository.findFirstByItemIdAndStatusAndStartIsAfterOrStartEqualsOrderByStart(
                eq(itemId), eq(Status.APPROVED), any(LocalDateTime.class), any(LocalDateTime.class)
        )).thenReturn(nextBooking);
        when(jpaBookingRepository.findFirstByItemIdAndStatusAndStartIsBeforeOrStartEqualsOrderByEndDesc(
                eq(itemId), eq(Status.APPROVED), any(LocalDateTime.class), any(LocalDateTime.class)
        )).thenReturn(lastBooking);

        ItemDtoResponse result = itemStorage.getItemAndBooking(itemId, ownerId);

        assertNull(result.getNextBooking());
        assertNull(result.getLastBooking());
    }

    @Test
    void getItemAndBooking_OwnerIdDoesNotMatch() {
        Integer itemId = 1, ownerId = 2;
        User owner = User.builder()
                .id(ownerId)
                .email("user1@gmail.com")
                .name("User1")
                .build();
        Item item = Item.builder()
                .available(true)
                .description("Item1 Description")
                .name("Item1 name")
                .request(1)
                .owner(owner)
                .build();
        item.setId(itemId);

        when(jpaItemRepository.findItemById(itemId)).thenReturn(item);

        ItemDtoResponse result = itemStorage.getItemAndBooking(itemId, ownerId);

        assertNull(result.getNextBooking(), "Next booking should be null when owner ID does not match");
        assertNull(result.getLastBooking(), "Last booking should be null when owner ID does not match");
    }
}
