package ru.practicum.shareit.request.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Request {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "description", nullable = false)
    String description;
    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    User requester;
    @Column(name = "created")
    LocalDateTime created;
    @OneToMany(mappedBy = "request", orphanRemoval = true, cascade = CascadeType.ALL)
    @Column(nullable = true)
    @JsonIgnore
    List<Item> items;
}