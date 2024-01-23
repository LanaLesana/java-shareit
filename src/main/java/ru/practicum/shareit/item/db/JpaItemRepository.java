package ru.practicum.shareit.item.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface JpaItemRepository extends JpaRepository<Item, Integer> {
    Item findItemById(Integer id);

    List<Item> findAllByOwnerId(Integer id);

    @Query("SELECT i FROM Item i " +
            "WHERE (upper(i.name) LIKE upper(concat('%', ?1, '%')) " +
            "   OR upper(i.description) LIKE upper(concat('%', ?1, '%'))) " +
            "   AND i.available = true")
    List<Item> search(String text);
}