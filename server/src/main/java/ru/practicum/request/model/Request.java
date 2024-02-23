package ru.practicum.request.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.item.model.Item;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
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
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "description", nullable = false)
    String description;
    @ManyToOne
    @JoinColumn(name = "requestor_id", nullable = false)
    User requestor;
    @Column(name = "created")
    LocalDateTime created;
    @OneToMany(mappedBy = "request", orphanRemoval = true, cascade = CascadeType.ALL)
    @Column(nullable = true)
    @JsonIgnore
    List<Item> items;
}
