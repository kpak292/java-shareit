package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(
        properties = "jdbc.url=jdbc:postgresql://localhost:5432/test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceIntegrationTests {

    final ItemService itemService;
    final UserService userService;

    @BeforeEach
    public void dataPreparation() {
        UserDto host = UserDto.builder()
                .email("test@test.uz")
                .name("host")
                .build();

        host = userService.create(host);

        ItemDto itemNotAvailableTextInName = ItemDto.builder()
                .name("ItemTeXtName")
                .description("ItemDescription")
                .available(false)
                .build();

        itemService.create(itemNotAvailableTextInName, host.getId());

        ItemDto itemNotAvailableTextInDesc = ItemDto.builder()
                .name("ItemName")
                .description("ItemTeXtDescription")
                .available(false)
                .build();

        itemService.create(itemNotAvailableTextInDesc, host.getId());

        ItemDto itemAvailableTextInName = ItemDto.builder()
                .name("ItemNameTeXt")
                .description("ItemDescription")
                .available(true)
                .build();

        itemService.create(itemAvailableTextInName, host.getId());

        ItemDto itemAvailableTextInDesc = ItemDto.builder()
                .name("ItemName")
                .description("TeXtItemDescription")
                .available(true)
                .build();

        itemService.create(itemAvailableTextInDesc, host.getId());

    }

    @Test
    public void findByTextTests() {
        Collection<ItemDto> itemDtos = itemService.findByText("text");

        assertEquals(2, itemDtos.size());
        ;
    }
}
