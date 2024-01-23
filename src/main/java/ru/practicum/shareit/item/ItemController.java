package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping
@AllArgsConstructor
public class ItemController {
    private final ItemServiceInterface itemService;
    private final Logger log = LoggerFactory.getLogger(ItemController.class);

    @PostMapping("/items")
    public ItemDto addItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int sharerUserId) {
        return itemService.addItem(itemDto, sharerUserId);
    }

    @PatchMapping("/items/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") int sharerUserId,
                              @PathVariable int itemId) {
        return itemService.updateItem(itemDto, sharerUserId, itemId);
    }

    @GetMapping("items/{itemId}")
    public ItemDtoResponse getItemById(@RequestHeader("X-Sharer-User-Id") Integer id, @PathVariable Integer itemId) {
        log.info("Метод - getItem, ");
        return itemService.getItemById(itemId, id);
    }

    @GetMapping("/items")
    public List<ItemDtoResponse> getAllItemsByOwner(@RequestHeader("X-Sharer-User-Id") int sharerUserId) {
        log.info("Получаю все предметы пользователя");
        return itemService.getAllItemsByOwnerId(sharerUserId);
    }

    @GetMapping("/items/search")
    public List<ItemDto> searchItem(@RequestParam(name = "text") String text) {
        return itemService.searchItem(text);
    }

    @PostMapping("/items/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Integer id,
                                 @PathVariable Integer itemId,
                                 @Valid @RequestBody CommentDto commentDto) {
        log.info("addComment id {},itemId {}, text {}", id, itemId, commentDto.getText());

        return itemService.addComment(id, itemId, commentDto);
    }
}
