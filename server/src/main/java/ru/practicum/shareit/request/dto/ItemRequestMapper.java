package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.request.entity.ItemRequest;

@Mapper(componentModel = "itemRequestServiceV1")
public interface ItemRequestMapper {
    ItemRequestMapper INSTANCE = Mappers.getMapper(ItemRequestMapper.class);

    ItemRequest getItemRequest(ItemRequestDto itemRequestDto);

    ItemRequestDto getItemRequestDto(ItemRequest itemRequest);
}
