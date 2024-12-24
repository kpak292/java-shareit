package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Collection<ItemDto>> findAllByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Request: GET/items from User {}", userId);

        Collection<ItemDto> items = itemService.findAllByUser(userId);
        log.info("Response: GET/items with body {}", items);

        return new ResponseEntity<>(
                items,
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> findById(@PathVariable long id, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Request: GET/items/{} from User {}", id, userId);

        ItemDto itemDto = itemService.findById(id, userId);
        log.info("Response: GET/items/{} with body {}", id, itemDto);

        return new ResponseEntity<>(
                itemDto,
                HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<ItemDto>> findByText(@RequestParam String text) {
        log.info("Request: GET/items/search text = {}", text);

        Collection<ItemDto> items = itemService.findByText(text);
        log.info("Response: GET/items/search with body {}", items);

        return new ResponseEntity<>(
                items,
                HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ItemDto> create(@Valid @RequestBody ItemDto itemDto,
                                          @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Request: POST/items from User {} with body {}", userId, itemDto);

        ItemDto item = itemService.create(itemDto, userId);
        log.info("Response: POST/items with body {}", item);

        return new ResponseEntity<>(
                item,
                HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDto> update(@PathVariable long id,
                                          @RequestBody ItemDto itemDto,
                                          @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Request: PATCH/items/{} from User {} with body {}", id, userId, itemDto);

        ItemDto item = itemService.update(id, itemDto, userId);
        log.info("Response: PATCH/items{} with body {}", id, item);

        return new ResponseEntity<>(
                item,
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ItemDto> remove(@PathVariable long id,
                                          @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Request: DELETE/items/{}", id);

        ItemDto itemDto = itemService.remove(id, userId);
        log.info("Response: DELETE/items/{} with body {}", id, itemDto);

        return new ResponseEntity<>(
                itemDto,
                HttpStatus.OK);
    }

}
