package ru.practicum.shareit.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaCommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByItem_Id(Integer itemId);

}
