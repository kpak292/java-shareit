package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.entity.Item;

@Mapper(componentModel = "itemServiceV1")
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    Item getItem(ItemDto itemDto);

    @Mapping(target = "hostId", expression = "java(item.getHost().getId())")
    ItemDto getItemDto(Item item);
}
