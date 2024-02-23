package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.db.JpaItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.db.JpaUserRepository;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private JpaItemRepository itemRepository;

    @Mock
    private JpaUserRepository userRepository;

    private ItemDto itemDto;
    private User user;
    private Item item;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);
        user.setName("Test User");
        user.setEmail("test@example.com");

        this.itemDto = ItemDto.builder()
                .ownerId(1)
                .name("ItemName")
                .description("ItemDescription")
                .available(true)
                .requestId(2)
                .build();

        when(userRepository.findUserById(anyInt())).thenReturn(user);
        this.item = new Item();
        item.setId(1);
        item.setOwner(user);
        item.setName("ItemName");
        item.setDescription("ItemDescription");
        item.setAvailable(true);
        item.setRequest(2);

        when(itemRepository.findItemById(anyInt())).thenReturn(item);
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item item = invocation.getArgument(0);
            item.setId(1);
            return item;
        });
    }

    @Test
    void addItemWithValidDataShouldReturnItemDto() {
        ItemDto result = itemService.addItem(itemDto, user.getId());

        assertNotNull(result);
        assertEquals(itemDto.getName(), result.getName());
        verify(userRepository, times(1)).findUserById(user.getId());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void addItemWithInvalidUserIdShouldThrowNotFoundException() {
        when(userRepository.findUserById(anyInt())).thenReturn(null);

        Exception exception = assertThrows(NotFoundException.class, () -> itemService.addItem(itemDto, 999));
        assertTrue(exception.getMessage().contains("Такого пользователя нет."));
    }

    @Test
    void addItemWithInvalidItemDataShouldThrowBadRequestException() {
        ItemDto invalidItemDto = new ItemDto();
        invalidItemDto.setOwnerId(1);
        invalidItemDto.setDescription("Description");

        Exception exception = assertThrows(BadRequestException.class, () -> itemService.addItem(invalidItemDto, user.getId()));
        assertTrue(exception.getMessage().contains("Укажите"));
    }

    @Test
    void updateItem_WithValidInputAndOwner_ShouldUpdateItem() {
        ItemDto updatedItemDto = itemService.updateItem(itemDto, user.getId(), item.getId());

        assertNotNull(updatedItemDto);
        assertEquals(itemDto.getName(), updatedItemDto.getName());
        assertEquals(itemDto.getDescription(), updatedItemDto.getDescription());
        assertEquals(itemDto.getAvailable(), updatedItemDto.getAvailable());
        verify(itemRepository, times(2)).findItemById(item.getId());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void updateItem_WithNonExistingItem_ShouldThrowNotFoundException() {
        when(itemRepository.findItemById(anyInt())).thenReturn(null);
        assertThrows(NotFoundException.class, () -> itemService.updateItem(itemDto, user.getId(), 2));
    }

    @Test
    void updateItem_WhenUserNotOwner_ShouldThrowNotFoundException() {
        User anotherUser = new User();
        anotherUser.setId(2);
        item.setOwner(anotherUser);
        when(itemRepository.findItemById(1)).thenReturn(item);

        assertThrows(NotFoundException.class, () -> itemService.updateItem(itemDto, user.getId(), item.getId()));
    }
}
