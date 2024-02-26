package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    Long id;
    @NotBlank
    String text;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDateTime created;
    String authorName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentDto)) return false;
        CommentDto that = (CommentDto) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getText(),
                that.getText()) && Objects.equals(getAuthorName(), that.getAuthorName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getText(), getAuthorName());
    }
}
