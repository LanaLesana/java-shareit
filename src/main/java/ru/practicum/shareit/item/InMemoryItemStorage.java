package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class InMemoryItemStorage implements ItemStorage {
    private HashMap<Integer, Item> itemHashMap = new HashMap<>();
    private Integer generatedItemId = 1;

    @Override
    public ItemDto addItem(ItemDto itemDto) {
        itemDto.setId(generatedItemId++);
        Item item = itemDto.fromItemDto(itemDto);
        itemHashMap.put(item.getId(), item);
        ItemDto itemDtoToReturn = itemHashMap.get(item.getId()).toItemDto(itemHashMap.get(item.getId()));
        return itemDtoToReturn;
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto) {
        Item item = itemDto.fromItemDto(itemDto);
        if (itemHashMap.containsKey(item.getId())) {
            Item itemToUpdate = itemHashMap.get(item.getId());

            if (item.getName() != null && !item.getName().equals(itemToUpdate.getName())) {
                itemToUpdate.setName(item.getName());
            }
            if (item.getDescription() != null && !item.getDescription().equals(itemToUpdate.getDescription())) {
                itemToUpdate.setDescription(item.getDescription());
            }
            if (item.getName() != null && !item.getName().equals(itemToUpdate.getName())) {
                itemToUpdate.setName(item.getName());
            }
            if (item.getAvailable() != null && item.getAvailable() && !itemToUpdate.getAvailable()) {
                itemToUpdate.setAvailable(true);
            }
            if (item.getAvailable() != null && !item.getAvailable() && itemToUpdate.getAvailable()) {
                itemToUpdate.setAvailable(false);
            }
            if (item.getOwner() != null) {
                if (item.getOwner().getId() != itemToUpdate.getOwner().getId()) {
                    itemToUpdate.setOwner(item.getOwner());
                }
            }
            itemHashMap.put(item.getId(), itemToUpdate);
        } else {
            return null;
        }
        return getItemById(item.getId());
    }

    @Override
    public ItemDto getItemById(int id) {
        if (itemHashMap.get(id) == null) {
            return null;
        } else {
            ItemDto itemDtoToReturn = itemHashMap.get(id).toItemDto(itemHashMap.get(id));
            return itemDtoToReturn;
        }
    }

    @Override
    public List<ItemDto> getAllItemsByOwnerId(int id) {
        Collection<Item> values = itemHashMap.values();

        List<ItemDto> ownerItems = values.stream()
                .filter(item -> item.getOwner().getId() == id)
                .map(Item::toItemDto)
                .collect(Collectors.toList());
        return ownerItems;
    }

    @Override
    public List<ItemDto> searchItem(String keyWord) {
        if (keyWord.isBlank() || keyWord == null) {
            return new ArrayList<ItemDto>();
        } else {
            Collection<Item> values = itemHashMap.values();

            List<Item> allAvailableItems = values.stream()
                    .filter(item -> item.getAvailable() == true)
                    .collect(Collectors.toList());

            List<ItemDto> filteredItems = allAvailableItems.stream()
                    .filter(item -> containsText(item, keyWord))
                    .map(Item::toItemDto)
                    .collect(Collectors.toList());

            return filteredItems;
        }
    }

    private static boolean containsText(Item item, String text) {
        text = text.toLowerCase();
        StringBuilder combinedText = new StringBuilder();
        combinedText.append(item.getName()).append(item.getDescription());
        return combinedText.toString().toLowerCase().contains(text);
    }
}
