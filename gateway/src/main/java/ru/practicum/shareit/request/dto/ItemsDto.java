package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemsDto {
    long id;

    long requestId;

    long itemId;

    String itemName;

    long hostId;

    LocalDateTime created;
}
