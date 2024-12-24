package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static Item getItem(ItemDto itemDto) {
        if (itemDto.getId() == null) {
            itemDto.setId(0L);
        }
        return Item.builder()
                .id(itemDto.getId())
                .available(itemDto.getAvailable())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .build();
    }

    public static ItemDto getItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
    }
}
