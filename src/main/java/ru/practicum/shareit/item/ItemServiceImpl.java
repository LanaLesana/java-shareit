package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.db.BookingStorage;
import ru.practicum.shareit.booking.db.JpaBookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.JpaCommentRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.db.ItemStorage;
import ru.practicum.shareit.item.db.JpaItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.validation.ItemValidation;
import ru.practicum.shareit.user.db.JpaUserRepository;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemServiceInterface {
    private final JpaItemRepository jpaItemRepository;
    private final JpaCommentRepository jpaCommentRepository;
    private final ItemStorage itemStorage;
    private final JpaUserRepository userStorage;
    private final BookingStorage bookingStorage;
    private final JpaBookingRepository jpaBookingRepository;
    UserDtoMapper userDtoMapper = new UserDtoMapper();
    private final ItemValidation itemValidation;
    private final MappingComment mappingComment;

    @Override
    @Transactional
    public ItemDto addItem(ItemDto itemDto, int sharerUserId) {
        if (itemDto.getAvailable() == null) {
            log.info("Checking availability");
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Укажите доступность предмета к аренде");
        }

        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            log.info("Checking name");
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Укажите имя предмета");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            log.info("Checking description");
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Укажите описание предмета");
        }

        User owner = userStorage.findUserById(sharerUserId);

        if (owner == null) {
            log.info("Couldn't find user by ID {}", sharerUserId);
            throw new NotFoundException("Такого пользователя нет.");
        } else {
            log.info("Found user with e-mail {}", owner.getEmail());
            itemDto.setOwnerId(owner.getId());
            log.info("Set owner with ID {} to the item", sharerUserId);
        }

        if (itemDto.getOwnerId() == null) {
            log.info("Couldn't find user with ID {}", sharerUserId);
            throw new NotFoundException("Такого пользователя нет.");
        } else {
            isValidItem(itemDto);
        }

        log.info("Adding item " + itemDto.getName());
        Item item = ItemMapper.toItem(owner,itemDto);
        item.setOwner(owner);
        item.setRequest((itemDto.getRequestId()));
        jpaItemRepository.save(item);
        Item returnedItem = jpaItemRepository.findItemById(item.getId());
        ItemDto returnedItemDto = ItemMapper.toItemDto(returnedItem);

        return returnedItemDto;
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto itemDto, int sharerUserId, int itemId) {
        if (jpaItemRepository.findItemById(itemId) == null) {
            throw new NotFoundException("Такого предмета нет.");
        }

        Item itemToCheckOwner = jpaItemRepository.findItemById(itemId);

        if (itemToCheckOwner.getOwner() == null || itemToCheckOwner.getOwner().getId() != sharerUserId) {
            log.info("Не совпадает идентификатор пользователя или нет такого пользователя");
            throw new NotFoundException("Не совпадает идентификатор пользователя");
        }

        itemDto.setId(itemId);
        itemDto.setOwnerId(itemToCheckOwner.getOwner().getId());
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            itemDto.setName(itemToCheckOwner.getName());
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            itemDto.setDescription(itemToCheckOwner.getDescription());
            ;
        }
        if (itemDto.getAvailable() == null) {
            itemDto.setAvailable(itemToCheckOwner.getAvailable());
        }

        Item item = ItemMapper.toItem(userStorage.findUserById(sharerUserId), itemDto);

        Item returnedItem = jpaItemRepository.save(item);
        ItemDto returnedItemDto = ItemMapper.toItemDto(returnedItem);
        return returnedItemDto;
    }


    @Override
    @Transactional
    public ItemDtoResponse getItemById(Integer itemId, Integer userId) {
        if (jpaItemRepository.findItemById(itemId) == null) {
            throw new NotFoundException("Такого предмета нет");
        } else {
            log.info("Getting item " + itemId);
            ItemDtoResponse returnedDto = itemStorage.getItemAndBooking(itemId, userId);
            itemValidation.checkingDataNull(returnedDto);
            return returnedDto;
        }
    }

    @Override
    public List<ItemDtoResponse> getAllItemsByOwnerId(int id) {
        if (id <= 0) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Неверно указан id пользователя");
        }
        return itemStorage.getAllByOwnerId(id);
    }

    @Override
    public List<ItemDto> searchItem(String keyWord) {
        if (keyWord == null || keyWord.isBlank()) {
            List<ItemDto> emptyList = new ArrayList<>();
            return emptyList;
        }
        List<Item> allItems = jpaItemRepository.search(keyWord);
        List<ItemDto> allItemDto = allItems.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        return allItemDto;
    }

    public void isValidItem(ItemDto item) {
        if (item.getName() == null || item.getName().isBlank()) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Не указано называние предмета");
        } else if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Не указано описание предмета");
        } else if (item.getOwnerId() == null) {
            throw new ValidationException("Не указан собственник предмета");
        }
    }

    @Override
    public ItemDtoResponse getItem(Integer itemId, Integer id) {
        itemValidation.checkItemId(jpaItemRepository.findItemById(itemId));
        ItemDtoResponse item = itemStorage.getItemAndBooking(itemId, id);
        itemValidation.checkingDataNull(item);
        return item;
    }

    @Override
    public List<ItemDtoResponse> getItemUser(Integer userId) {
        return itemStorage.getAllByOwnerId(userId);

    }

    @Override
    @Transactional
    public CommentDto addComment(Integer idUser, Integer itemId, CommentDto commentDto) {
        if (itemStorage.getItem(itemId) == null) {
            throw new NotFoundException("Такого предмета нет.");
        }
        LocalDateTime start = bookingStorage.getBookingById(itemId).getStart();
        itemValidation.checkComment(commentDto.getText(), start);
        List<Booking> bookings = jpaBookingRepository.findAllByBooker_IdAndItem_Id(idUser, itemId);
        itemValidation.checkCommentBooking(idUser, bookings);
        LocalDateTime localDateTime = LocalDateTime.now();
        Item item = itemStorage.getItem(itemId);
        User user = userStorage.findUserById(idUser);
        Comment comment = Comment.builder()
                .text(commentDto.getText())
                .item(item)
                .author(user)
                .created(localDateTime).build();
        return mappingComment.mappingCommentInCommentDto(jpaCommentRepository.save(comment));
    }

}



