package ru.practicum.shareit.comment;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CommentDto {
    private Integer id;
    @NotBlank
    private String text;
    private Integer itemId;
    private String authorName;
    private LocalDateTime created;
}
