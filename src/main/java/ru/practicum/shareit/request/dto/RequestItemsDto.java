package ru.practicum.shareit.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class RequestItemsDto {
    long id;

    long requestId;

    long itemId;

    String itemName;

    long hostId;

    LocalDateTime created;
}
