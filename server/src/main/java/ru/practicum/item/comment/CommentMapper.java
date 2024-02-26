package ru.practicum.item.comment;

import lombok.experimental.UtilityClass;
import ru.practicum.item.comment.Comment;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.model.Item;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .authorName(comment.getAuthor().getName())
                .build();
    }

    public static Comment toComment(User user, Item item, CommentDto commentDto, LocalDateTime created) {
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setText(commentDto.getText());
        comment.setItem(item);
        comment.setCreated(created);
        return comment;
    }
}
