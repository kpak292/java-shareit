package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UnauthorizedAccessException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.List;

@Service("itemServiceV1")
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    @Qualifier("userServiceV1")
    private UserService userService;


    @Override
    public Collection<ItemDto> findAllByUser(long userId) {
        userService.findById(userId);

        return itemRepository.findByUserId(userId).stream()
                .map(ItemMapper.INSTANCE::getItemDto)
                .toList();
    }

    @Override
    public ItemDto findById(long id, long userId) {
        userService.findById(userId);

        return ItemMapper.INSTANCE.getItemDto(itemRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Item is not found with id = " + id +
                        "related to user with id = " + userId)));
    }

    @Override
    public Collection<ItemDto> findByText(String text) {
        if (text==null||text.isBlank()){
            return List.of();
        }

        return itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text, text).stream()
                .filter(Item::isAvailable)
                .map(ItemMapper.INSTANCE::getItemDto)
                .toList();
    }

    @Override
    public ItemDto create(ItemDto itemDto, long userId) {
        userService.findById(userId);

        Item item = ItemMapper.INSTANCE.getItem(itemDto);
        item.setUserId(userId);

        return ItemMapper.INSTANCE.getItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto update(long id, ItemDto itemDto, long userId) {
        userService.findById(userId);
        Item item = itemRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Item is not found with id = " + id +
                        "related to user with id = " + userId));

        if (item.getUserId() != userId) {
            throw new UnauthorizedAccessException("User " + userId + "has no rights to change item " + id);
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.INSTANCE.getItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto remove(long id, long userId) {
        userService.findById(userId);
        Item item = itemRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Item is not found with id = " + id +
                        "related to user with id = " + userId));

        itemRepository.deleteById(id);

        return ItemMapper.INSTANCE.getItemDto(item);
    }
}
