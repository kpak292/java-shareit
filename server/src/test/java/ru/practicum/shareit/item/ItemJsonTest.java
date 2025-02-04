package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemJsonTest {
    private final JacksonTester<ItemDto> json;

    @Test
    public void itemDtoJsonTest() throws Exception {
        LocalDateTime commentDate = LocalDateTime.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

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

        JsonContent<ItemDto> content = json.write(itemDto);

        assertThat(content).extractingJsonPathNumberValue("$.id").isEqualTo(5);
        assertThat(content).extractingJsonPathStringValue("$.name").isEqualTo("ItemName");
        assertThat(content).extractingJsonPathStringValue("$.description").isEqualTo("ItemDescription");
        assertThat(content).extractingJsonPathBooleanValue("$.available").isEqualTo(false);
        assertThat(content).extractingJsonPathNumberValue("$.comments.[0].id").isEqualTo(2);
        assertThat(content).extractingJsonPathStringValue("$.comments.[0].text").isEqualTo("CommentText");
        assertThat(content).extractingJsonPathStringValue("$.comments.[0].authorName").isEqualTo("authorName");
        assertThat(content).extractingJsonPathStringValue("$.comments.[0].created").isEqualTo(commentDate.format(formatter));
        assertThat(content).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(3);
        assertThat(content).extractingJsonPathNumberValue("$.nextBooking.id").isEqualTo(4);
        assertThat(content).extractingJsonPathNumberValue("$.requestId").isEqualTo(6);
        assertThat(content).extractingJsonPathNumberValue("$.hostId").isEqualTo(7);
    }
}
