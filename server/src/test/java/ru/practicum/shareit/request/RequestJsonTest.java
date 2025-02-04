package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestItemsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestJsonTest {
    private final JacksonTester<RequestItemsDto> requestItemsJson;
    private final JacksonTester<ItemRequestDto> itemRequestJson;

    @Test
    public void RequestDtoJsonTest() throws Exception {
        LocalDateTime created = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        RequestItemsDto requestItemsDto = RequestItemsDto.builder()
                .id(1L)
                .requestId(2L)
                .itemId(3L)
                .itemName("itemName")
                .hostId(4L)
                .created(created)
                .build();

        JsonContent<RequestItemsDto> content = requestItemsJson.write(requestItemsDto);

        assertThat(content).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(content).extractingJsonPathNumberValue("$.requestId").isEqualTo(2);
        assertThat(content).extractingJsonPathNumberValue("$.itemId").isEqualTo(3);
        assertThat(content).extractingJsonPathStringValue("$.itemName").isEqualTo("itemName");
        assertThat(content).extractingJsonPathNumberValue("$.hostId").isEqualTo(4);
        assertThat(content).extractingJsonPathStringValue("$.created").isEqualTo(created.format(formatter));
    }

    @Test
    public void ItemRequestJsonTest() throws Exception {
        LocalDateTime created = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        ItemDto itemDto = ItemDto.builder()
                .id(5L)
                .name("ItemName")
                .description("ItemDescription")
                .available(false)
                .requestId(1L)
                .hostId(7L)
                .build();

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("description")
                .created(created)
                .items(List.of(itemDto))
                .userId(2L)
                .build();

        JsonContent<ItemRequestDto> content = itemRequestJson.write(itemRequestDto);

        assertThat(content).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(content).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(content).extractingJsonPathStringValue("$.created").isEqualTo(created.format(formatter));
        assertThat(content).extractingJsonPathNumberValue("$.items.[0].id").isEqualTo(5);
        assertThat(content).extractingJsonPathStringValue("$.items.[0].name").isEqualTo("ItemName");
        assertThat(content).extractingJsonPathStringValue("$.items.[0].description").isEqualTo("ItemDescription");
        assertThat(content).extractingJsonPathBooleanValue("$.items.[0].available").isEqualTo(false);
        assertThat(content).extractingJsonPathNumberValue("$.items.[0].requestId").isEqualTo(1);
        assertThat(content).extractingJsonPathNumberValue("$.items.[0].hostId").isEqualTo(7);
        assertThat(content).extractingJsonPathNumberValue("$.userId").isEqualTo(2);
    }
}
