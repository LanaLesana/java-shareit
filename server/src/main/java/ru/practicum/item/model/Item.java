package ru.practicum.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.booking.model.Booking;
import ru.practicum.item.comment.Comment;

import javax.persistence.*;
import java.util.List;


/**
 * TODO Sprint add-controllers.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
@Table(name = "items")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "item_name", nullable = false)
    String name;
    @Column(name = "description", nullable = false)
    String description;
    @Column(name = "owner_id", nullable = false)
    Long ownerId;
    @Column(name = "is_available")
    Boolean available;
    @Column
    Long request;
    @OneToMany(mappedBy = "item")
    List<Booking> bookings;
    @OneToMany(mappedBy = "item")
    List<Comment> comments;


}
