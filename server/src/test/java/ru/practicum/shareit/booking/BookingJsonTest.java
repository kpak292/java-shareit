package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.entity.State;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingJsonTest {
    private final JacksonTester<BookingDto> json;

    @Test
    public void bookingDtoJsonTest() throws Exception {
        LocalDateTime commentDate = LocalDateTime.now().minusDays(1);
        LocalDateTime start = LocalDateTime.now().minusDays(2);
        LocalDateTime end = LocalDateTime.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("test@test.uz")
                .name("name")
                .build();

        CommentDto commentDto = CommentDto.builder()
                .id(2L)
                .text("CommentText")
                .authorName("authorName")
                .created(commentDate)
                .build();

        BookingDto lastBooking = BookingDto.builder()
                .id(3L)
                .build();

        BookingDto nextBooking = BookingDto.builder()
                .id(4L)
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(5L)
                .name("ItemName")
                .description("ItemDescription")
                .available(false)
                .comments(List.of(commentDto))
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .requestId(6L)
                .hostId(7L)
                .build();

        BookingDto bookingDto = BookingDto.builder()
                .id(8L)
                .booker(userDto)
                .itemId(9L)
                .item(itemDto)
                .start(start)
                .end(end)
                .status(State.WAITING)
                .build();

        JsonContent<BookingDto> content = json.write(bookingDto);

        assertThat(content).extractingJsonPathNumberValue("$.id").isEqualTo(8);
        assertThat(content).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(content).extractingJsonPathStringValue("$.booker.email").isEqualTo("test@test.uz");
        assertThat(content).extractingJsonPathStringValue("$.booker.name").isEqualTo("name");
        assertThat(content).extractingJsonPathNumberValue("$.itemId").isEqualTo(9);
        assertThat(content).extractingJsonPathNumberValue("$.item.id").isEqualTo(5);
        assertThat(content).extractingJsonPathStringValue("$.item.name").isEqualTo("ItemName");
        assertThat(content).extractingJsonPathStringValue("$.item.description").isEqualTo("ItemDescription");
        assertThat(content).extractingJsonPathBooleanValue("$.item.available").isEqualTo(false);
        assertThat(content).extractingJsonPathNumberValue("$.item.comments.[0].id").isEqualTo(2);
        assertThat(content).extractingJsonPathStringValue("$.item.comments.[0].text").isEqualTo("CommentText");
        assertThat(content).extractingJsonPathStringValue("$.item.comments.[0].authorName").isEqualTo("authorName");
        assertThat(content).extractingJsonPathStringValue("$.item.comments.[0].created").isEqualTo(commentDate.format(formatter));
        assertThat(content).extractingJsonPathNumberValue("$.item.lastBooking.id").isEqualTo(3);
        assertThat(content).extractingJsonPathNumberValue("$.item.nextBooking.id").isEqualTo(4);
        assertThat(content).extractingJsonPathNumberValue("$.item.requestId").isEqualTo(6);
        assertThat(content).extractingJsonPathNumberValue("$.item.hostId").isEqualTo(7);
        assertThat(content).extractingJsonPathStringValue("$.start").isEqualTo(start.format(formatter));
        assertThat(content).extractingJsonPathStringValue("$.end").isEqualTo(end.format(formatter));
        assertThat(content).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }
}
