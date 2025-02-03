package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.Collection;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    Long id;

    @NotBlank
    @Size(max = 255)
    String name;

    @NotBlank
    @Size(max = 255)
    String description;

    @NotNull
    Boolean available;

    Collection<CommentRequestDto> comments;

    BookingRequestDto lastBooking;

    BookingRequestDto nextBooking;

    Long requestId;

    Long hostId;
}
