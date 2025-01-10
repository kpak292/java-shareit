package ru.practicum.shareit.booking.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.entity.State;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    long id;

    UserDto booker;

    @Nullable
    Long itemId;

    ItemDto item;

    //Иногда дает ощибку валидации при постман тестах!!!
    @NotNull(message = "Start cannot be null")
    @FutureOrPresent(message = "Start date cannot be in past")
    LocalDateTime start;

    @NotNull(message = "End cannot be null")
    @FutureOrPresent(message = "End date cannot be in past")
    LocalDateTime end;

    State status;

    @AssertTrue(message = "start and end cannot be equal")
    private boolean isEqual() {
        if (start == null || end == null) {
            return true;
        }

        return !start.isEqual(end);
    }
}
