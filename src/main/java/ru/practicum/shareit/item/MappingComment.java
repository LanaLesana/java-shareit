package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentDto;

@Service
public class MappingComment {
    public CommentDto mappingCommentInCommentDto(Comment comment1) {
        CommentDto commentDto1 = CommentDto.builder()
                .id(comment1.getId())
                .text(comment1.getText())
                .itemId(comment1.getItem().getId())
                .authorName(comment1.getAuthor().getName())
                .created(comment1.getCreated()).build();
        return commentDto1;
    }
}
