package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.entity.Item;

@Mapper(componentModel = "itemServiceV1")
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    Item getItem(ItemDto itemDto);

    ItemDto getItemDto(Item item);
}
