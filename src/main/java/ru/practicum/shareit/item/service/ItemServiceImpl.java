package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UnauthorizedAccessException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@Service("itemServiceV1")
public class ItemServiceImpl implements ItemService {
    @Autowired
    @Qualifier("inMemoryItemRepository")
    private ItemRepository itemRepository;

    @Autowired
    @Qualifier("userServiceV1")
    private UserService userService;


    @Override
    public Collection<ItemDto> findAllByUser(long userId) {
        userService.findById(userId);

        return itemRepository.findAllByUser(userId).stream()
                .map(ItemMapper.INSTANCE::getItemDto)
                .toList();
    }

    @Override
    public ItemDto findById(long id, long userId) {
        userService.findById(userId);

        return ItemMapper.INSTANCE.getItemDto(itemRepository.findById(id));
    }

    @Override
    public Collection<ItemDto> findByText(String text) {
        return itemRepository.findByText(text).stream()
                .map(ItemMapper.INSTANCE::getItemDto)
                .toList();
    }

    @Override
    public ItemDto create(ItemDto itemDto, long userId) {
        userService.findById(userId);

        Item item = ItemMapper.INSTANCE.getItem(itemDto);
        item.setUserId(userId);

        return ItemMapper.INSTANCE.getItemDto(itemRepository.create(item));
    }

    @Override
    public ItemDto update(long id, ItemDto itemDto, long userId) {
        userService.findById(userId);
        Item item = itemRepository.findById(id);

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

        return ItemMapper.INSTANCE.getItemDto(itemRepository.update(item));
    }

    @Override
    public ItemDto remove(long id, long userId) {
        userService.findById(userId);
        Item item = itemRepository.findById(id);

        if (item.getUserId() != userId) {
            throw new UnauthorizedAccessException("User " + userId + "has no rights to change item " + id);
        }

        return ItemMapper.INSTANCE.getItemDto(itemRepository.remove(id));
    }
}
