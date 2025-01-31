package ru.practicum.shareit.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    Long id;


    String name;


    String email;
}
