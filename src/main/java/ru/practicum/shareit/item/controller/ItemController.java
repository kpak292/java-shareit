package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.config.Constants;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    @Qualifier("itemServiceV1")
    private ItemService itemService;

    @GetMapping()
    public ResponseEntity<Collection<ItemDto>> findAllByUser(@RequestHeader(Constants.HEADER_FOR_USER_ID) long userId) {
        Collection<ItemDto> items = itemService.findAllByUser(userId);
        log.info("GET/items from USER {} with response {}", userId, items);

        return new ResponseEntity<>(
                items,
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> findById(@PathVariable long id,
                                            @RequestHeader(Constants.HEADER_FOR_USER_ID) long userId) {
        ItemDto itemDto = itemService.findById(id, userId);
        log.info("GET/items/{} from User {} with response {}", id, userId, itemDto);

        return new ResponseEntity<>(
                itemDto,
                HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<ItemDto>> findByText(@RequestParam String text) {
        Collection<ItemDto> items = itemService.findByText(text);
        log.info("GET/items/search?text={} with response {}", text, items);

        return new ResponseEntity<>(
                items,
                HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ItemDto> create(@Valid @RequestBody ItemDto itemDto,
                                          @RequestHeader(Constants.HEADER_FOR_USER_ID) long userId) {
        ItemDto item = itemService.create(itemDto, userId);
        log.info("POST/items from User {} with response {}", userId, item);

        return new ResponseEntity<>(
                item,
                HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDto> update(@PathVariable long id,
                                          @RequestBody ItemDto itemDto,
                                          @RequestHeader(Constants.HEADER_FOR_USER_ID) long userId) {
        ItemDto item = itemService.update(id, itemDto, userId);
        log.info("PATCH/items{} from User {} with response {}", id, userId, item);

        return new ResponseEntity<>(
                item,
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ItemDto> remove(@PathVariable long id,
                                          @RequestHeader(Constants.HEADER_FOR_USER_ID) long userId) {
        ItemDto itemDto = itemService.remove(id, userId);
        log.info("DELETE/items/{} with body {}", id, itemDto);

        return new ResponseEntity<>(
                itemDto,
                HttpStatus.OK);
    }

}
