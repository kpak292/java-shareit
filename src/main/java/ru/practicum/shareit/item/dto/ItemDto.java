package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    Long id;

    @NotBlank
    @Size(max = 255)
    String name;

    @NotBlank
    @Size(max = 255)
    String description;

    @NotNull
    Boolean available;

    Collection<CommentDto> comments;

    BookingDto lastBooking;

    BookingDto nextBooking;
}
