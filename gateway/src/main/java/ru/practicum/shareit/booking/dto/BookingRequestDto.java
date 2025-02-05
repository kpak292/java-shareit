package ru.practicum.shareit.booking.dto;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.dto.UserRequestDto;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequestDto {
    private long id;

    UserRequestDto userRequestDto;

    @Nullable
    private Long itemId;

    @NotNull(message = "Start cannot be null")
    @FutureOrPresent(message = "Start date cannot be in past")
    private LocalDateTime start;

    @NotNull(message = "Start cannot be null")
    @FutureOrPresent(message = "Start date cannot be in past")
    private LocalDateTime end;

    BookingState status;

    @AssertTrue(message = "start and end cannot be equal")
    private boolean isEqual() {
        if (start == null || end == null) {
            return true;
        }

        return !start.isEqual(end);
    }
}
