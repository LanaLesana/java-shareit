package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.Empty;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private int id;
    private String name;
    @Email
    @Nullable
    private String email;

}
