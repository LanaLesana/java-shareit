package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService implements ItemServiceInterface {
    @Autowired
    ItemStorage itemStorage;
    @Autowired
    UserStorage userStorage;
    UserDtoMapper userDtoMapper = new UserDtoMapper();

    @Override
    public ItemDto addItem(ItemDto itemDto, int sharerUserId) {
        if (itemDto.getAvailable() == null) {
            throw new BadRequestException("Укажите доступность предмета к аренде");
        }
        UserDto ownerDto = userStorage.getUserById(sharerUserId);
        if (ownerDto == null) {
            throw new NotFoundException("Такого пользователя нет.");
        } else {
            User owner = userDtoMapper.fromUserDto(ownerDto);
            itemDto.setOwner(owner);
        }

        if (itemDto.getOwner() == null) {
            throw new NotFoundException("Такого пользователя нет.");
        } else {
            isValidItem(itemDto);
        }

        log.info("Adding item " + itemDto.getName());
        ItemDto returnedItemDto = itemStorage.addItem(itemDto);
        return returnedItemDto;
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, int sharerUserId, int itemId) {
        ItemDto itemToCheckOwner = itemStorage.getItemById(itemId);
        if (itemToCheckOwner.getOwner() == null || itemToCheckOwner.getOwner().getId() != sharerUserId) {
            throw new NotFoundException("Не совпадает идентификатор пользователя");
        }
        itemDto.setId(itemId);

        if (itemStorage.updateItem(itemDto) == null) {
            throw new BadRequestException("Такого предмета нет");
        } else {
            log.info("Updating item " + itemDto.getName());
            ItemDto returnedItem = itemStorage.getItemById(itemDto.getId());
            return returnedItem;
        }
    }

    @Override
    public ItemDto getItemById(int id) {
        if (itemStorage.getItemById(id) == null) {
            throw new BadRequestException("Такого предмета нет");
        } else {
            log.info("Getting item " + id);
            ItemDto returnedDto = itemStorage.getItemById(id);
            return returnedDto;
        }
    }

    @Override
    public List<ItemDto> getAllItemsByOwnerId(int id) {
        if (id <= 0) {
            throw new BadRequestException("Неверно указан id пользователя");
        }
        return itemStorage.getAllItemsByOwnerId(id);
    }

    @Override
    public List<ItemDto> searchItem(String keyWord) {
        return itemStorage.searchItem(keyWord);
    }

    public void isValidItem(ItemDto item) {
        if (item.getName() == null || item.getName().isBlank()) {
            throw new BadRequestException("Не указано называние предмета");
        } else if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new BadRequestException("Не указано описание предмета");
        } else if (item.getOwner() == null) {
            throw new ValidationException("Не указан собственник предмета");
        }
    }
}



