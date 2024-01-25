package ru.practicum.shareit.item.db;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.db.JpaBookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingConcise;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.JpaCommentRepository;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.*;

@Component
@AllArgsConstructor
public class ItemStorage {
    JpaItemRepository jpaItemRepository;
    JpaBookingRepository jpaBooking;
    JpaCommentRepository jpaCommentRepository;

    public Item addItem(Item item) {

        return jpaItemRepository.save(item);
    }


    public Item getItem(Integer id) {
        return jpaItemRepository.findItemById(id);
    }

    public ItemDtoResponse getItemAndBooking(Integer id, Integer ownerId) {
        LocalDateTime localDateTime = LocalDateTime.now();

        Item item = jpaItemRepository.findItemById(id);

        Booking nextBooking = jpaBooking.findFirstByItemIdAndStatusAndStartIsAfterOrStartEqualsOrderByStart(id,
                Status.APPROVED, localDateTime, localDateTime);
        Booking lastBooking = jpaBooking.findFirstByItemIdAndStatusAndStartIsBeforeOrStartEqualsOrderByEndDesc(id,
                Status.APPROVED, localDateTime, localDateTime);

        BookingConcise bookingConciseLast = null;
        BookingConcise bookingConciseNext = null;

        if (item.getOwner().getId() == ownerId) {
            if (nextBooking == null) {
                bookingConciseNext = null;
            } else {
                bookingConciseLast = BookingConcise.builder().id(nextBooking.getId())
                        .bookerId(nextBooking.getBooker().getId()).build();
            }
            if (lastBooking == null) {
                bookingConciseLast = null;
            } else {
                bookingConciseNext = BookingConcise.builder().id(lastBooking.getId())
                        .bookerId(lastBooking.getBooker().getId()).build();
            }
        } else {
            bookingConciseLast = null;
            bookingConciseNext = null;
        }
        List<CommentDto> commentDtoList = new ArrayList<>();
        List<Comment> comments = jpaCommentRepository.findAllByItem_Id(item.getId());
        for (Comment comment : comments) {
            CommentDto commentDto = CommentDto.builder()
                    .id(comment.getId())
                    .text(comment.getText())
                    .itemId(comment.getItem().getId())
                    .authorName(comment.getAuthor().getName())
                    .created(comment.getCreated()).build();
            commentDtoList.add(commentDto);
        }
        ItemDtoResponse itemDtoResponse = ItemDtoResponse.builder().id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner().getId())
                //.request(item.getRequest().getDescription())
                .nextBooking(bookingConciseLast)
                .comments(commentDtoList)
                .lastBooking(bookingConciseNext).build();
        return itemDtoResponse;
    }

    public List<ItemDtoResponse> getAllByOwnerId(Integer userId) {
        List<ItemDtoResponse> itemDtoResponses = new ArrayList<>();
        List<Item> itemList = jpaItemRepository.findAllByOwnerId(userId);
        List<Booking> allbookings = jpaBooking.findAll();
        for (Item item : itemList) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            List<Booking> bookingListByItemId = new ArrayList<>();
            List<LocalDateTime> dateTimesStart = new ArrayList<>();
            List<LocalDateTime> dateTimesEnd = new ArrayList<>();

            for (Booking booking : allbookings) {
                if (Objects.equals(booking.getItem().getId(), item.getId()) && Status.APPROVED == booking.getStatus()) {
                    bookingListByItemId.add(booking);
                    dateTimesStart.add(booking.getStart());
                    dateTimesEnd.add(booking.getEnd());
                }
            }

            Booking nextBooking = null;
            Booking lastBooking = null;
            LocalDateTime closestPastDateTime = null;
            LocalDateTime firstPastDateTime = null;

            for (LocalDateTime dateTime : dateTimesStart) {
                if (dateTime.isBefore(currentDateTime)) {
                    if (closestPastDateTime == null || dateTime.isAfter(closestPastDateTime)) {
                        closestPastDateTime = dateTime;
                    }
                }
            }

            for (LocalDateTime dateTime : dateTimesEnd) {
                if (dateTime.isAfter(currentDateTime)) {
                    if (firstPastDateTime == null || dateTime.isBefore(firstPastDateTime)) {
                        firstPastDateTime = dateTime;
                    }
                }
            }


            for (Booking booking : bookingListByItemId) {
                if (booking.getStart().equals(closestPastDateTime)) {
                    lastBooking = booking;
                }
            }

            for (Booking booking : bookingListByItemId) {
                if (booking.getEnd().equals(firstPastDateTime)) {
                    nextBooking = booking;
                }
            }
            BookingConcise bookingConciseLast;
            BookingConcise bookingConciseNext;

            if (lastBooking != null && nextBooking != null) {
                bookingConciseLast = BookingConcise.builder().id(nextBooking.getId())
                        .bookerId(nextBooking.getBooker().getId()).build();
                bookingConciseNext = BookingConcise.builder().id(lastBooking.getId())
                        .bookerId(lastBooking.getBooker().getId()).build();
            } else {
                bookingConciseLast = null;
                bookingConciseNext = null;
            }

            ItemDtoResponse itemDtoResponse = ItemDtoResponse.builder().id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .owner(item.getOwner().getId())
                    //.request(item.getRequest().getDescription())
                    .nextBooking(bookingConciseLast)
                    .lastBooking(bookingConciseNext).build();
            itemDtoResponses.add(itemDtoResponse);
            nextBooking = null;
            lastBooking = null;
            bookingConciseNext = null;
            bookingConciseLast = null;
        }
        return itemDtoResponses;
    }

    public List<Item> findAllByNameOrDescription(String string) {
        return jpaItemRepository.search(string);
    }

}