package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.request.entity.RequestItems;

@Mapper(componentModel = "itemRequestServiceV1")
public interface RequestItemsMapper {
    RequestItemsMapper INSTANCE = Mappers.getMapper(RequestItemsMapper.class);

    RequestItems getrequestItems(RequestItemsDto requestItemsDto);

    @Mapping(target = "itemId", expression = "java(requestItems.getItem().getId())")
    @Mapping(target = "itemName", expression = "java(requestItems.getItem().getName())")
    @Mapping(target = "hostId", expression = "java(requestItems.getItem().getHost().getId())")
    @Mapping(target = "requestId", expression = "java(requestItems.getRequest().getId())")
    RequestItemsDto getRequestItemsDto(RequestItems requestItems);
}
